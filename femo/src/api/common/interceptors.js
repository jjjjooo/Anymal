//import store from '@/store/memberStore';
export function setIntercepotors(instance) {
  instance.interceptors.request.use(
    function (config) {
      const accessToken =
        localStorage.getItem('ACCESS_TOKEN');
      //config.headers.Authorization = `Bearer ${store.state.token}`;
      config.headers.Authorization =
        'Bearer ' + accessToken;
      return config;
    },
    function (error) {
      return Promise.reject(error);
    },
  );

  //axios.post('/signup') - 인스턴스x
  //instance.interceptoers- 로하자
  instance.interceptors.response.use(
    function (response) {
      return response;
    },
    function (error) {
      return Promise.reject(error);
    },
  );
  return instance;
}
