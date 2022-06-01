<template>
  <v-container>
    <v-row justify="center">
      <v-col align="center">
        <v-btn
          small
          class="mt-4"
          color="primary"
          dark
          to="/protect-post"
          >더 보기</v-btn
        ></v-col
      >
    </v-row>
    <v-row>
      <v-col align="start">
        <p class="font-weight-black">찾고 있어요</p>
      </v-col>
    </v-row>
    <v-divider></v-divider>
    <v-row>
      <v-col cols="12">
        <v-slide-group class="pa-4" multiple show-arrows>
          <v-slide-item v-for="n in postList" :key="n.id">
            <v-card class="ma-4">
              <router-link :to="`/post-detail/${n.id}`">
                <v-img
                  class="white--text align-end"
                  :src="`http://localhost:9000/thumbnail/${n.thumbnailId}`"
                  height="200"
                  width="150"
                >
                </v-img>
              </router-link>
            </v-card>
          </v-slide-item>
        </v-slide-group>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col align="start">
        <p class="font-weight-black">보호 중이에요</p>
      </v-col>
    </v-row>
    <v-divider></v-divider>
    <v-row>
      <v-col cols="12">
        <v-slide-group class="pa-4" multiple show-arrows>
          <v-slide-item v-for="n in postList2" :key="n.id">
            <v-card class="ma-4">
              <router-link :to="`/post-detail/${n.id}`">
                <v-img
                  class="white--text align-end"
                  :src="`http://localhost:9000/thumbnail/${n.thumbnailId}`"
                  height="200"
                  width="150"
                >
                </v-img>
              </router-link>
            </v-card>
          </v-slide-item>
        </v-slide-group>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    category: '1',
    items: '',
    categories: [
      {
        text: '보호 중',
        filter: '1',
      },
      {
        text: '찾는 중',
        filter: '2',
      },
    ],
  }),

  computed: {
    postList() {
      return this.$store.state.postStore.postList;
    },
    postList2() {
      return this.$store.state.postStore.postList2;
    },
  },
  methods: {
    async select(category) {
      this.category = category.filter;
    },
  },
  created() {
    let form = {
      page: 1,
      dType: 'pr',
    };
    this.$store.dispatch('REQUEST_GET_ALL_POST_PAGE', form);
    let form2 = {
      page: 1,
      dType: 'ms',
    };
    this.$store.dispatch(
      'REQUEST_GET_ALL_POST_PAGE',
      form2,
    );
  },
};
</script>
