<template>
  <v-app-bar
    v-scroll="onScroll"
    app
    elevate-on-scroll
    :color="!isScrolling ? 'transparent' : 'grey lighten-3'"
  >
    <v-img
      class="shrink"
      max-width="100%"
      max-height="100%"
    />

    <template v-if="$vuetify.breakpoint.mdAndUp">
      <v-btn
        v-for="(item, i) in items"
        :key="i"
        :to="item.to"
        class="font-weight-bold"
        text
      >
        <span v-text="item.text" />
      </v-btn>
      <v-spacer />
    </template>
    <template v-else>
      <v-menu>
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>
        <v-card color="primary">
          <v-list>
            <v-list-item
              v-for="(item, i) in items"
              :key="i"
              :to="item.to"
            >
              <v-list-item-title v-text="item.text" />
            </v-list-item>
          </v-list>
        </v-card>
      </v-menu>
      <v-spacer />
    </template>
    <v-btn
      v-if="!isLogin"
      to="/auth/sign-in"
      text
      class="font-weight-bold"
    >
      <v-icon large>mdi-account</v-icon>
    </v-btn>
    <v-menu
      v-else
      v-model="showMenu"
      absolute
      offset-y
      style="max-width: 600px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          text
          class="font-weight-bold"
          v-bind="attrs"
          v-on="on"
        >
          <v-icon large>mdi-account-circle</v-icon>
        </v-btn>
      </template>

      <v-navigation-drawer v-model="showMenu">
        <v-system-bar></v-system-bar>
        <v-list>
          <v-list-item>
            <v-list-item-avatar>
              <v-icon>mdi-account-circle</v-icon>
            </v-list-item-avatar>
            <v-list-item-content>
              <v-list-item-title class="text-h6">
                {{ username }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
        <v-divider></v-divider>
        <v-list nav dense>
          <v-list-item-group color="primary">
            <v-list-item
              v-for="(info, i) in infoes"
              :key="i"
              :to="info.to"
            >
              <v-list-item-icon>
                <v-icon v-text="info.icon"></v-icon>
              </v-list-item-icon>

              <v-list-item-content>
                <v-list-item-title
                  v-text="info.text"
                ></v-list-item-title>
              </v-list-item-content>
            </v-list-item>
            <v-list-item>
              <v-list-item-icon>
                <v-icon>mdi-logout</v-icon>
              </v-list-item-icon>
              <v-list-item-content v-on:click="logOut">
                <v-list-item-title>
                  로그아웃
                </v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-navigation-drawer>
    </v-menu>
  </v-app-bar>
</template>

<script>
// Utilities
import { mapMutations } from 'vuex';
import { deleteCookie } from '@/utils/cookies';
export default {
  data: () => ({
    isScrolling: false,
    loginName: '',
    items: [
      {
        to: '/home',
        text: 'Home',
      },
      {
        to: '/protect-post',
        text: '더 보기',
      },
    ],
    showMenu: false,
    infoes: [
      { text: '내정보', icon: 'mdi-account-outline' },

      {
        text: '좋아요 게시글',
        icon: 'mdi-thumb-up',
        to: '/auth/my-good',
      },
      {
        text: '내 게시글',
        icon: 'mdi-format-float-left',
        to: '/auth/my-post',
      },
      {
        text: '내 대화',
        icon: 'mdi-wechat',
        to: '/chat/room',
      },
    ],
  }),

  methods: {
    ...mapMutations(['toggleDrawer']),
    onScroll() {
      this.isScrolling =
        (window.pageYOffset ||
          document.documentElement.scrollTop ||
          0) > 20;
    },
    logOut() {
      this.$store.commit('clearMember');
      // deleteCookie('til_auth');
      // deleteCookie('til_user');
      localStorage.removeItem('NAME');
      localStorage.removeItem('ACCESS_TOKEN');
      this.$router.push('/home');
    },
  },
  computed: {
    isLogin() {
      return this.$store.getters.isLogin;
    },
    username() {
      return this.$store.state.memberStore.username;
    },
  },
};
</script>
