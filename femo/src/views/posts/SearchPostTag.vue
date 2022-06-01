<template>
  <v-app>
    <v-container>
      <v-row>
        <v-col
          v-for="(list, index) in tagList"
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
          </v-tabs-items>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-pagination
          v-model="page"
          :length="totalPage"
          circle
          outlined
          total-visible="16"
          @input="fetchTagPage(page)"
        ></v-pagination>
      </v-row>
      <v-spacer />
      <v-row justify="center" no-gutters class="mt-6">
        <v-col cols="3">
          <v-text-field
            label="태그로 검색하기"
            solo
            v-model="search"
            v-on:keydown.enter.prevent="searchTag(search)"
            @click:append-outer="searchTag(search)"
            append-outer-icon="mdi-magnify"
          >
          </v-text-field>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
export default {
  name: 'MainList',

  data() {
    return {
      page: '',
      search: '',
    };
  },

  methods: {
    searchTag(tag) {
      let form = {
        tag: tag,
        page: 1,
      };
      this.$store.dispatch('REQUEST_GET_TAG_POST', form);
    },
    toAdd() {
      console.log(this.username);
      if (this.username !== '') {
        this.$router.push('/add-form');
      } else {
        alert('로그인 후 가능합니다.');
      }
    },

    fecthPostPage(tag, page) {
      this.$store.commit('SET_POST_CURRENT_PAGE', page);
      let form = {
        tag: tag,
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
    tagList() {
      return this.$store.state.postStore.tagList;
    },
  },
  created() {
    this.page = this.currentPage;
  },
  mounted() {
    let form = {
      tag: this.$route.params.tag,
      id: this.$route.params.id,
      page: 1,
    };
    this.$store.dispatch('REQUEST_GET_TAG_POST', form);
  },
};
</script>

<style></style>
