package com.micrantha.bluebell.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


class EmailVisualTransformation(
    private val visibleCharsPrefix: Int = 2, // Number of visible characters at the start of username
    private val visibleCharsSuffix: Int = 1, // Number of visible characters at the end of username (before @)
    private val visibleDomainPrefix: Int = 1, // Number of visible characters at the start of domain
    private val visibleDomainSuffix: Int = 2, // Number of visible characters at the end of domain (after the last dot)
    private val maskChar: Char = '*'
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isBlank()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val atIndex = originalText.lastIndexOf('@')

        val transformedString = if (atIndex == -1) {
            // No '@' symbol, transform as if it's all username (or don't transform)
            transformPart(originalText, visibleCharsPrefix, visibleCharsSuffix, maskChar)
        } else {
            val username = originalText.substring(0, atIndex)
            val domainWithTld = originalText.substring(atIndex + 1)

            val transformedUsername = transformPart(username, visibleCharsPrefix, visibleCharsSuffix, maskChar)
            val transformedDomain = transformDomainPart(domainWithTld, visibleDomainPrefix, visibleDomainSuffix, maskChar)

            "$transformedUsername@$transformedDomain"
        }

        return TransformedText(
            AnnotatedString(transformedString),
            EmailOffsetMapping(originalText, transformedString)
        )
    }

    private fun transformPart(part: String, prefixLen: Int, suffixLen: Int, mask: Char): String {
        if (part.length <= prefixLen + suffixLen) {
            return part // Not enough characters to mask
        }
        val prefix = part.substring(0, prefixLen)
        val suffix = part.substring(part.length - suffixLen)
        val middleMasked = mask.toString().repeat(part.length - prefixLen - suffixLen)
        return "$prefix$middleMasked$suffix"
    }

    private fun transformDomainPart(domainWithTld: String, prefixLen: Int, suffixLen: Int, mask: Char): String {
        val lastDotIndex = domainWithTld.lastIndexOf('.')
        if (lastDotIndex == -1 || lastDotIndex == 0 || lastDotIndex == domainWithTld.length - 1) {
            // No TLD or malformed, transform the whole thing as one part
            return transformPart(domainWithTld, prefixLen, 0, mask) // Use 0 for suffix to avoid issues with short domains
        }

        val domainName = domainWithTld.substring(0, lastDotIndex)
        val tld = domainWithTld.substring(lastDotIndex + 1) // Top-level domain

        transformPart(domainName, prefixLen, 0, mask) // Mask middle of domain name
        // Usually, we want to keep the TLD visible, or at least its common parts like .com, .org
        // The current `visibleDomainSuffix` logic applies to the *entire* domainWithTld,
        // which might not be ideal if you want to preserve the TLD specifically.
        // For simplicity, this example transforms the domain name part and keeps the TLD.
        // A more robust approach might be to show first char of domain, mask, then show TLD.

        // Simpler approach for domain: show first N of domain name, mask rest of domain name, show TLD
        if (domainName.length <= prefixLen) {
            return "$domainName.$tld"
        }
        val domainPrefix = domainName.substring(0, prefixLen)
        val domainMasked = mask.toString().repeat(domainName.length - prefixLen)

        return "$domainPrefix$domainMasked.$tld"
    }
}

private class EmailOffsetMapping(
    private val originalText: String,
    private val transformedText: String
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        // This is a simplified mapping. For a truly accurate mapping with masking,
        // you need to account for which parts of the string are masked
        // and how their lengths change.
        // A common strategy is to map to the nearest unmasked character or the end of a masked segment.

        val atIndexOriginal = originalText.lastIndexOf('@')
        val atIndexTransformed = transformedText.lastIndexOf('@')

        if (atIndexOriginal == -1) { // No '@'
            return mapPartOffset(offset, originalText, transformedText, 2, 1)
        }

        return if (offset <= atIndexOriginal) { // Cursor in username part
            mapPartOffset(offset,
                originalText.substring(0, atIndexOriginal),
                transformedText.substring(0, atIndexTransformed),
                2, 1 // Assuming visibleCharsPrefix=2, visibleCharsSuffix=1 for username
            )
        } else { // Cursor in domain part
            // Offset for domain part needs to be relative to the start of the domain
            val domainOffset = offset - (atIndexOriginal + 1)
            val originalDomain = originalText.substring(atIndexOriginal + 1)
            val transformedDomain = transformedText.substring(atIndexTransformed + 1)

            atIndexTransformed + 1 + mapPartOffset(
                domainOffset,
                originalDomain,
                transformedDomain,
                1, 2 // Assuming visibleDomainPrefix=1, visibleDomainSuffix=2 for domain
                // Note: The transformDomainPart logic is a bit different, so this mapping needs care
            )
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        // This is also simplified. A robust implementation would trace back from
        // the transformed offset to the corresponding original position.
        val atIndexOriginal = originalText.lastIndexOf('@')
        val atIndexTransformed = transformedText.lastIndexOf('@')

        if (atIndexTransformed == -1) { // No '@' in transformed (implies none in original either)
            return unmapPartOffset(offset, transformedText, originalText, 2, 1)
        }

        return if (offset <= atIndexTransformed) { // Cursor in transformed username part
            unmapPartOffset(offset,
                transformedText.substring(0, atIndexTransformed),
                originalText.substring(0, atIndexOriginal),
                2, 1
            )
        } else { // Cursor in transformed domain part
            val domainOffset = offset - (atIndexTransformed + 1)
            val transformedDomain = transformedText.substring(atIndexTransformed + 1)
            val originalDomain = originalText.substring(atIndexOriginal + 1)

            atIndexOriginal + 1 + unmapPartOffset(
                domainOffset,
                transformedDomain,
                originalDomain,
                1, 2
            )
        }
    }

    // Helper for mapping offsets within a part (username or domain name)
    private fun mapPartOffset(offset: Int, originalPart: String, transformedPart: String, prefixLen: Int, suffixLen: Int): Int {
        if (offset <= prefixLen) return offset
        if (offset > originalPart.length - suffixLen) {
            // Cursor is in the suffix part
            // The length difference is (originalPart.length - transformedPart.length)
            // The new offset = prefixLen + (masked_segment_len_in_transformed) + (cursor_pos_in_suffix_original - 1)
            return transformedPart.length - (originalPart.length - offset)
        }
        // Cursor is in the masked middle part
        // Map to the end of the visible prefix or start of the masked segment
        return prefixLen + (offset - prefixLen).coerceAtMost(transformedPart.length - prefixLen - suffixLen)
    }

    // Helper for unmapping offsets
    private fun unmapPartOffset(offset: Int, transformedPart: String, originalPart: String, prefixLen: Int, suffixLen: Int): Int {
        if (offset <= prefixLen) return offset
        if (offset >= transformedPart.length - suffixLen) {
            // Cursor is in the suffix part of the transformed string
            return originalPart.length - (transformedPart.length - offset)
        }
        // Cursor is in the masked middle part
        // Map to the corresponding position in the original middle part
        // This assumes the masked part in transformed corresponds one-to-one or collapses.
        // For a more robust solution, you'd need to know how many chars were masked.
        // A simple approach: if in masked region, could map to start of original masked region + progress
        return prefixLen + (offset - prefixLen).coerceAtMost(originalPart.length - prefixLen - suffixLen)

    }
}