import {
  fetchPosts,
  fetchPost,
  requestGood,
  deletePost,
  updatePost,
  requestGetMyPosts,
  requestGetMyGoodPosts,
  requestGetTagPosts,
} from '@/api/posts';

const postStore = {
  state: {
    tagList: null,
    searchList: [],
    postList: [],
    postList2: [],
    postDetail: '',
    myPostList: [],
    myGoodPostList: '',
    postTotalPage: 0,
    postCurrentPage: 1,
    postGood: false,
    myPostTotalPage: 1,
    myPostCurrentPage: 1,
  },
  getters: {
    good(state) {
      return state.postGood;
    },
  },
  mutations: {
    SET_POST_SEARCH(state, payload) {
      state.searchList = payload;
    },
    SET_POST_LIST(state, payload) {
      state.postList = payload;
    },
    SET_POST_LIST2(state, payload) {
      state.postList2 = payload;
    },
    SET_POST_DETAIL(state, payload) {
      state.postDetail = payload;
    },
    SET_MY_POST_LIST(state, payload) {
      state.myPostList = payload;
    },
    SET_MY_GOOD_POST_LIST(state, payload) {
      state.myGoodPostList = payload;
    },
    SET_POST_TOTAL_PAGE(state, payload) {
      state.postTotalPage = payload;
    },
    SET_POST_CURRENT_PAGE(state, payload) {
      state.postCurrentPage = payload;
    },
    SET_POST_GOOD(state, payload) {
      state.postGood = payload;
    },
    SET_MY_POST_TOTAL_PAGE(state, payload) {
      state.myPostTotalPage = payload;
    },
    SET_TAG_SEARCH(state, payload) {
      state.tagList = payload;
    },
  },

  actions: {
    async REQUEST_PUSH_GOOD(context, payload) {
      const { data } = await requestGood(payload);
      const good = context.getters.good;
      context.commit('SET_POST_GOOD', !good);
      return data;
    },
    async REQUEST_GET_POST({ commit }, payload) {
      const { data } = await fetchPost(payload);
      console.log(data);
      commit('SET_POST_GOOD', data.good);
      commit('SET_POST_DETAIL', data);
    },

    async REQUEST_GET_ALL_POST_PAGE({ commit }, payload) {
      const response = await fetchPosts(payload);
      console.log(response.data);
      if (response.data.data[0].dtype === 'pr') {
        commit('SET_POST_LIST', response.data.data);
        commit(
          'SET_POST_TOTAL_PAGE',
          response.data.totalPage,
        );
      } else if (response.data.data[0].dtype === 'ms') {
        commit('SET_POST_LIST2', response.data.data);
        commit(
          'SET_POST_TOTAL_PAGE',
          response.data.totalPage,
        );
      }
    },
    async REQUEST_GET_SEARCH_POST({ commit }, payload) {
      const response = await fetchPosts(payload);
      console.log(response.data);
      commit('SET_POST_SEARCH', response.data.data);
      commit(
        'SET_POST_TOTAL_PAGE',
        response.data.totalPage,
      );
    },
    async REQUEST_DELETE_POST(context, payload) {
      const response = await deletePost(payload);
      if (response) {
        alert('정상적으로 삭제되었습니다.');
      }
      await this.router.push('/protect-post');
    },
    async REQUEST_UPDATE_POST(context, payload) {
      const response = await updatePost(payload);
      if (response) {
        alert('정상적으로 수정되었습니다.');
      }
    },
    async REQUEST_GET_MY_POST(context, payload) {
      const response = await requestGetMyPosts(payload);
      console.log(response.data);
      if (response) {
        context.commit('SET_MY_POST_LIST', response.data);
        context.commit(
          'SET_MY_POST_TOTAL_PAGE',
          response.data.totalPage,
        );
      }
    },
    async REQUEST_GET_TAG_POST(context, paylod) {
      const { data } = await requestGetTagPosts(paylod);
      console.log(data);

      context.commit('SET_TAG_SEARCH', data);
    },
    async REQUEST_GET_MY_GOOD_POST(context, payload) {
      const response = await requestGetMyGoodPosts(payload);
      console.log(response.data);
      if (response) {
        context.commit(
          'SET_MY_GOOD_POST_LIST',
          response.data,
        );
      }
    },
  },
};
export default postStore;
