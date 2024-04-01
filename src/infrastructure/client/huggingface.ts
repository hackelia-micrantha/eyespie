import {HfInference} from '@huggingface/inference';
import type {Options} from '@huggingface/inference';

interface Params {
  negative_prompt?: string;
  prompt?: string;
}

export class HuggingFaceClient {
  hf: HfInference;

  constructor() {
    this.hf = new HfInference(process.env.REACT_APP_HUGGING_FACE_TOKEN);
  }

  classifyInImage = (imageData: Blob) =>
    this.hf.imageClassification({
      data: imageData,
      model: 'google/vit-base-patch16-224',
    });

  detectInImage = async (imageData: Blob) =>
    this.hf.objectDetection({
      data: imageData,
      model: 'facebook/detr-resnet-50',
    });

  generateImageFromText = async (description: string, parameters?: Params) =>
    this.hf.textToImage({
      inputs: description,
      model: 'stabilityai/stable-diffusion-2',
      parameters,
    });

  generateImageFromImage = async (imageData: Blob, parameters?: Params) =>
    this.hf.imageToImage({
      inputs: imageData,
      parameters,
      model: 'lllyasviel/sd-controlnet-depth',
    });

  matchImageToLabels = async (imageData: Blob, labels: Array<string>) =>
    this.hf.zeroShotImageClassification({
      model: 'openai/clip-vit-large-patch14-336',
      inputs: {
        image: imageData,
      },
      parameters: {
        candidate_labels: labels,
      },
    });
}
