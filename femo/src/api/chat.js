import { chatting } from '@/api/index';

//게시글 채팅방만들기
function requestAddChatRoom(data) {
  return chatting.post('/', data);
}

//채팅방 기록조회
function requestGetChatLogs(data) {
  return chatting.get(data.roomId, {
    params: { name: data.username },
  });
}
//전체 채팅방 조회
function requestGetChatRooms(data) {
  return chatting.get('/', {
    params: { name: data },
  });
}

//읽지않은 메시지 조회
function requestGetNotReadCount(data) {
  return chatting({
    url: 'read',
    method: 'GET',
    params: { name: data },
  });
}

//채팅방 삭제 -> 삭제목록 이동

export default {
  requestAddChatRoom,
  requestGetChatLogs,
  requestGetChatRooms,
  requestGetNotReadCount,
};
