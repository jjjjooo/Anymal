import chat from '@/api/chat.js';
import router from '@/router/index';

const chatStore = {
  state: {
    chatLog: [],
    chatRooms: [],
    chatRoomDetail: '',
    chatCount: '',
  },
  mutations: {
    SET_CHAT_ROOMS(state, payload) {
      state.chatRooms = payload;
    },
    SET_CHAT_COUNT(state, payload) {
      state.chatCount = payload;
    },
    SET_CHAT_ROOM_DETAIL(state, payload) {
      state.chatRoomDetail = payload;
    },
  },
  actions: {
    async REQUEST_ADD_CHATROOM(context, payload) {
      const response = await chat.requestAddChatRoom(
        payload,
      );

      if (response) {
        await router.push({
          path: '/chat',
          query: {
            roomId: response.data,
            sender: payload.sender,
            receiver: payload.receiver,
            postId: payload.postId,
          },
        });
        console.log(response);
      }
    },
    async REQUEST_GET_CHAT_LOGS(context, payload) {
      const response = await chat.requestGetChatLogs(
        payload,
      );
      if (response) {
        console.log(response.data);
        context.commit(
          'SET_CHAT_ROOM_DETAIL',
          response.data,
        );
      }
    },
    async REQUEST_GET_CHAT_ROOMS(context, payload) {
      console.log(payload);
      const response = await chat.requestGetChatRooms(
        payload,
      );

      if (response) {
        console.log(response.data);
        context.commit('SET_CHAT_ROOMS', response.data);
      }
    },
    async REQUEST_GET_CHAT_COUNT(context, payload) {
      const response = await chat.requestGetNotReadCount(
        payload,
      );
      if (response) {
        console.log(response.data);
        context.commit('SET_CHAT_COUNT', response.data);
      }
    },
  },
};

export default chatStore;
