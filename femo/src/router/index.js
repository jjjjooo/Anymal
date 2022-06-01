import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Index',
    component: () => import('@/views/main/Index.vue'),
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/main/Home.vue'),
      },
      {
        path: '/protect-post',
        name: 'Protect',
        component: () =>
          import('@/views/posts/MainList.vue'),
      },
      {
        path: '/add-form',
        name: 'AddForm',
        component: () =>
          import('@/views/posts/AddForm.vue'),
      },
      {
        path: '/post-detail/:id',
        name: 'PostDetail',
        component: () =>
          import('@/views/posts/DetailPost.vue'),
      },
      {
        path: '/auth/my-post',
        component: () => import('@/views/auth/MyPost'),
      },

      {
        path: '/auth/my-good',
        component: () => import('@/views/auth/MyGoodPost'),
      },
      {
        path: '/chat/room',
        component: () => import('@/views/auth/ChatRoom'),
      },
      {
        path: '/search-tag',
        name: 'search',
        component: () =>
          import('@/views/posts/SearchPostTag'),
        props: true,
      },
      {
        path: '/chat',
        component: () => import('@/components/Chat'),
      },
      {
        path: '/chat/room',
        component: () => import('@/views/auth/ChatRoom'),
      },
    ],
  },
  {
    path: '/auth/sign-in',
    component: () => import('@/views/auth/SignIn'),
  },
  {
    path: '/auth/sign-up',
    component: () => import('@/views/auth/SignUp'),
  },
];

const router = new VueRouter({
  mode: 'history',
  routes,
});

export default router;
