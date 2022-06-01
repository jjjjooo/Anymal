<template>
  <v-container>
    <v-responsive height="200vh" class="text-center pa-2">
      <v-lazy
        v-model="isActive"
        :options="{
          threshold: 0.5,
        }"
        min-height="200"
        transition="fade-transition"
      >
        <v-row>
          <v-col
            v-for="(list, index) in myPostList"
            :key="index"
            cols="3"
            align="center"
          >
            <v-card
              max-width="400"
              elevation="10"
              outlined
              shaped
              class="auto"
              ><router-link :to="`/post-detail/${list.id}`">
                <v-img
                  :src="`http://localhost:9000/thumbnail/${list.thumbnailId}`"
                  height="200px"
                  width="auto"
                  class="white--text align-end"
                ></v-img
              ></router-link>
              <div v-if="list.dtype === 'pr'">
                <v-card-text class="font-weight-bold">
                  [보호]{{ list.title }}
                </v-card-text>
              </div>
              <div v-if="list.dtype === 'ms'">
                <v-card-text class="font-weight-bold">
                  [분실]{{ list.title }}
                </v-card-text>
              </div>
              <v-divider></v-divider>
              <v-card-text>
                <v-chip-group
                  mandatory
                  show-arrows
                  active-class="primary--text"
                >
                  <v-chip
                    small
                    outlined
                    v-for="tag in list.tag"
                    :key="tag.id"
                  >
                    {{ tag.tag }}
                  </v-chip>
                </v-chip-group>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-lazy>
    </v-responsive>
  </v-container>
</template>

<script>
export default {
  name: 'MyPost',

  data() {
    return {
      names: {
        originName: '',
        requestusername: '',
      },
      paramusername: '',
      page: 1,
      isActive: false,
    };
  },
  methods: {
    showPostByusername() {
      let data = {
        page: this.myPostCurrentPage,
        username: this.paramusername,
        type: '보호',
      };
      this.$store.dispatch('REQUEST_GET_MY_POST', data);
    },
    showMyPostPage(page) {
      console.log('page : ' + page);
      this.$store.commit('SET_MY_Post_CURRENT_PAGE', page);
      let data = {
        page: page,
        name: this.paramusername,
        type: '보호',
      };
      this.$store.dispatch('REQUEST_GET_MY_POST', data);
    },
    async pushLike(PostId) {
      await this.$store
        .dispatch('REQUEST_PUSH_INTEREST', PostId)
        .then(() => {
          let data = {
            page: this.myPostCurrentPage - 1,
            name: this.paramusername,
            type: '보호',
          };
          this.$store.dispatch('REQUEST_GET_MY_POST', data);
        });
    },
  },
  computed: {
    myPostList() {
      return this.$store.state.postStore.myPostList;
    },
    username() {
      return this.$store.state.memberStore.username;
    },
    myPostTotalPage() {
      return this.$store.state.postStore.myPostTotalPage;
    },
    myPostCurrentPage() {
      return this.$store.state.postStore.myPostCurrentPage;
    },
  },
  created() {
    this.paramUsername = this.$route.query.username;
    this.names.requestUsername = this.$route.query.username;
    this.names.originName = this.username;
    this.page = this.myPostCurrentPage;
  },
  mounted() {
    this.showPostByusername();
  },
};
</script>

<style scoped></style>
