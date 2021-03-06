import axios from 'axios';

import { setIntercepotors } from '@/api/common/interceptors.js';

//μΌλ°
function createInstance() {
  const instance = axios.create();
  return instance;
}
//νμ
function createInstanceWithAuth(url) {
  const instance = axios.create({
    baseURL: `/api/${url}`,
    // baseURL: `${process.env.VUE_APP_API_URL}${url}`,
    // baseURL: `${process.env.dev.VUE_APP_API_URL}${url}`,
  });

  return setIntercepotors(instance);
}

export const instance = createInstance();

export const auth = createInstanceWithAuth('auth');
export const posts = createInstanceWithAuth('post');
export const chatting = createInstanceWithAuth('chat');
