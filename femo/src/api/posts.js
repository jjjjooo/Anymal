import { posts } from '@/api/index.js';

//게시글 생성
function createPosts(postData) {
  return posts.post('/', postData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}

//게시글 수정
function updatePost(payload) {
  return posts.put('/', payload, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
}
//게시글 페이징 조회
function fetchPosts(data) {
  console.log(data.page);
  return posts.get(`/list/${data.page}`, {
    params: { dType: data.dType, area: data.area },
  });
}
//게시글 개별조회
function fetchPost(data) {
  return posts.get(data);
}
//게시글 좋아요
function requestGood(data) {
  return posts.post('/good/' + data);
}
//게시글 삭제
function deletePost(data) {
  return posts.delete(`/${data}`);
}
//내 게시글
function requestGetMyPosts(data) {
  return posts.get(`/my/${data.page}`, {
    params: { name: data.username, dType: data.type },
  });
}
//내 좋아요 게시글
function requestGetMyGoodPosts(data) {
  return posts.get('/my/good', { params: { name: data } });
}

function requestGetTagPosts(data) {
  return posts.get('/tag', {
    params: {
      tag: data.tag,
      tagId: data.id,
      page: data.page,
    },
  });
}

export {
  fetchPosts,
  createPosts,
  fetchPost,
  requestGood,
  deletePost,
  updatePost,
  requestGetMyPosts,
  requestGetMyGoodPosts,
  requestGetTagPosts,
};
