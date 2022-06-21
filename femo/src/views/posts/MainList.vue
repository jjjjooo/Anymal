<template>
  <v-app>
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-tabs
            background-color="transparent"
            centered
            class="mb-5"
            show-arrows
          >
            <v-tab
              v-for="(category, i) in categories"
              :key="i"
              @click="select(category)"
            >
              <span v-text="category.text" />
            </v-tab>
          </v-tabs>
        </v-col>
      </v-row>
      <v-row>
        <v-col
          v-for="(list, index) in postList"
          :key="index"
          cols="3"
          sm="3"
          align="center"
          ><v-tabs-items>
            <v-card
              max-width="400"
              elevation="10"
              outlined
              shaped
              class="auto"
              ><router-link :to="`/post-detail/${list.id}`">
                <v-img
                  :src="`http://ec2-3-37-84-38.ap-northeast-2.compute.amazonaws.com/thumbnail/${list.thumbnailId}`"
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
                    @click="searchTag(tag.tag, tag.id)"
                  >
                    {{ tag.tag }}
                  </v-chip>
                </v-chip-group>
              </v-card-text>
            </v-card>
          </v-tabs-items>
        </v-col>
      </v-row>
      <v-row justify="center">
        <div class="text-center">
          <v-pagination
            v-model="page"
            :length="totalPage"
            circle
            outlined
            total-visible="16"
            @input="fecthPostPage(page)"
          ></v-pagination>
          <v-btn
            color="primary"
            class="mt-2"
            @click="toAdd"
            outlined
            >글쓰기</v-btn
          >
        </div>
        <add-post />
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
export default {
  name: 'MainList',

  data() {
    return {
      lists: '',
      category: '1',
      categories: [
        {
          text: '보호 중',
          filter: '1',
        },
        {
          text: '찾는 중',
          filter: '2',
        },
        {
          text: '거주지 주변',
          filter: '3',
        },
      ],
      page: 1,
    };
  },
  watch: {
    category: async function (category) {
      if (category === '1') {
        let form = {
          page: 1,
          dType: 'pr',
        };
        this.$store.dispatch(
          'REQUEST_GET_SEARCH_POST',
          form,
        );
      }
      if (category === '2') {
        let form = {
          page: 1,
          dType: 'ms',
        };
        this.$store.dispatch(
          'REQUEST_GET_SEARCH_POST',
          form,
        );
      }
      if (category === '3') {
        let form = {
          page: 1,
          area: this.userarea,
        };
        this.$store.dispatch(
          'REQUEST_GET_SEARCH_POST',
          form,
        );
      }
    },
  },
  methods: {
    searchTag(tag, id) {
      this.$router.push({
        name: 'search',
        params: { tag: tag, id: id },
      });
    },
    toAdd() {
      console.log(this.username);
      if (this.username !== '') {
        this.$router.push('/add-form');
      } else {
        alert('로그인 후 가능합니다.');
      }
    },
    async select(category) {
      this.category = category.filter;
    },
    fecthPostList() {
      let form = {
        page: this.currentPage,
        dType: 'pr',
      };
      this.$store.dispatch('  ', form);
    },
    fecthPostPage(page) {
      this.$store.commit('SET_POST_CURRENT_PAGE', page);
      let form = {
        dType: 'ms',
        page: this.currentPage,
      };
      this.$store.dispatch(
        'REQUEST_GET_ALL_POST_PAGE',
        form,
      );
    },
  },
  computed: {
    totalPage() {
      return this.$store.state.postStore.postTotalPage;
    },
    currentPage() {
      return this.$store.state.postStore.postCurrentPage;
    },
    postList() {
      return this.$store.state.postStore.searchList;
    },
    username() {
      return this.$store.state.memberStore.username;
    },
    userarea() {
      return this.$store.state.memberStore.address.sigungu;
    },
  },
  created() {
    this.page = this.currentPage;
  },
  mounted() {
    let form = {
      page: 1,
      dType: 'pr',
    };
    this.$store.dispatch('REQUEST_GET_SEARCH_POST', form);
    console.log(this.userarea);
  },
};
</script>

<style></style>
