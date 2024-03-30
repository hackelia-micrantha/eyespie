import {loadModel, predict} from '@huggingface/inference';

export const performInference = async text => {
  const model = await loadModel('model_name');
  const result = await predict(model, text);
  return result;
};
