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
            v-for="(list, index) in myGoodPostList"
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
  data() {
    return {
      isActive: false,
    };
  },
  methods: {
    async pushGood(productId) {
      await this.$store
        .dispatch('REQUEST_PUSH_INTEREST', productId)
        .then(() => {
          this.$store.dispatch(
            'REQUEST_GET_MY_GOOD_POST',
            this.username,
          );
        });
    },
  },
  computed: {
    myGoodPostList() {
      return this.$store.state.postStore.myGoodPostList;
    },
    username() {
      return this.$store.state.memberStore.username;
    },
  },
  created() {},
  mounted() {
    this.$store.dispatch(
      'REQUEST_GET_MY_GOOD_POST',
      this.username,
    );
    console.log(this.myGoodPostList);
  },
};
</script>

<style scoped></style>
