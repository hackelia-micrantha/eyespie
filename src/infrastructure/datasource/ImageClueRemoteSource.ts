import {HuggingFaceClient} from '@infrastructure/client';
import {Clues} from '@domain/model';

export class ImageClueRemoteSource {
  client: HuggingFaceClient;

  constructor() {
    this.client = new HuggingFaceClient();
  }
  classify = async (imageData: Blob) => this.client.classifyInImage(imageData);

  detect = async (imageData: Blob) => this.client.detectInImage(imageData);

  match = async (imageData: Blob, clues: Clues) =>
    this.client.matchImageToLabels(
      imageData,
      clues.labels.map(label => label.text),
    );
}
