import { auth } from '@/api/index.js';

//회원가입
function registerMember(memberData) {
  return auth.post('/signup', memberData);
}

//로그인
function loginMember(memberData) {
  return auth.post('/login', memberData);
}

function getInfo() {
  return auth.get('/');
}

function updateMember(memberData) {
  return auth.put('/', memberData);
}

function deleteMember(memberData) {
  return auth.delete('/', {
    params: { password: memberData.password },
  });
}

//회원탈퇴

//회원수정
export {
  registerMember,
  loginMember,
  getInfo,
  updateMember,
  deleteMember,
};
