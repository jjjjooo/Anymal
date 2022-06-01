import Vue from 'vue';
import Vuex from 'vuex';

import memberStore from '@/store/memberStore';
import postStore from '@/store/postStore';
import chatStore from '@/store/chatStore';
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

export const store = new Vuex.Store({
  // ...
  modules: {
    memberStore,
    postStore,
    chatStore,
  },
  plugins: [
    createPersistedState({
      paths: ['memberStore'],
    }),
  ],
});
export default store;
