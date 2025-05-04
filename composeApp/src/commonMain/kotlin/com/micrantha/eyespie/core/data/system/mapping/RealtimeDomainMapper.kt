package com.micrantha.eyespie.core.data.system.mapping

import com.micrantha.eyespie.domain.entities.RealtimeAction
import com.micrantha.eyespie.domain.entities.RealtimeAction.Add
import com.micrantha.eyespie.domain.entities.RealtimeAction.Modify
import com.micrantha.eyespie.domain.entities.RealtimeAction.Query
import com.micrantha.eyespie.domain.entities.RealtimeAction.Remove
import com.micrantha.eyespie.domain.entities.Thing
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.PostgresAction.Delete
import io.github.jan.supabase.realtime.PostgresAction.Insert
import io.github.jan.supabase.realtime.PostgresAction.Select
import io.github.jan.supabase.realtime.PostgresAction.Update
import io.github.jan.supabase.realtime.decodeOldRecord
import io.github.jan.supabase.realtime.decodeRecord

class RealtimeDomainMapper {
    fun thing(action: PostgresAction): RealtimeAction<Thing> = when (action) {
        is Insert -> Add(action.decodeRecord())
        is Update -> Modify(action.decodeRecord(), action.decodeOldRecord())
        is Delete -> Remove(action.decodeOldRecord())
        is Select -> Query(action.decodeRecord())
    }
}
