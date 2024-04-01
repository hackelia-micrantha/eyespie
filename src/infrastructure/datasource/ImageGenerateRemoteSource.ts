import {HuggingFaceClient} from '@infrastructure/client';

export class ImageGenerateRemoteSource {
  client: HuggingFaceClient;

  constructor() {
    this.client = new HuggingFaceClient();
  }

  generate = async (): Promise<Blob> => {
    // join clues into text description
    const text = '';
    // create prompt from clues
    const prompt = '';
    return this.client.generateImageFromText(text, {prompt});
  };

  generateOneShot = async (imageData: Blob): Promise<Blob> => {
    // create prompt from clues
    const prompt = '';
    return this.client.generateImageFromImage(imageData, {prompt});
  };
}
