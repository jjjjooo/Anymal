<template>
  <!-- <v-container>
    <v-row> -->
  <!-- <v-text-field
        text
        label="주소"
        v-model="address"
        prepend-icon="mdi-home-outline"
        disabled
        class="mr-4"
      ></v-text-field>
      <v-btn
        class="mt-2"
        color="red lighten-2"
        dark
        :disabled="!updateFlag"
        @click="DaumPost()"
        >주소 찾기</v-btn
      > -->

  <!-- <v-dialog v-model="postOpen" width="500">
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            color="red lighten-2"
            dark
            v-bind="attrs"
            v-on="on"
          >
            주소 찾기
          </v-btn>
        </template>

        <VueDaumPostcode @complete="oncomplete" />
      </v-dialog> -->
  <section class="test">
    <div>
      <v-text-field
        text
        label="주소"
        v-model="address"
        prepend-icon="mdi-email-outline"
        v-on:click="search"
        :disabled="!updateFlag"
      />
    </div>
    <div class="post-box" v-if="postOpen">
      <template>
        <VueDaumPostcode @complete="oncomplete" />
      </template>
    </div>
  </section>
  <!-- </v-row>
  </v-container> -->
</template>
<script>
import { VueDaumPostcode } from 'vue-daum-postcode';

export default {
  props: {
    updateFlag: {
      type: Boolean,
    },
  },
  components: {
    VueDaumPostcode,
  },
  data() {
    return {
      address: '',
      postOpen: false,
    };
  },
  computed: {
    postDetail() {
      return this.$store.state.postStore.postDetail;
    },
  },
  methods: {
    search: function () {
      this.postOpen = true;
    },
    oncomplete: function (result) {
      if (result.userSelectedType === 'R') {
        // 도로명 주소 선택
        this.address = result.roadAddress;
      } else {
        // 지번 주소 선택
        this.address = result.jibunAddress;
      }
      this.postOpen = false;
      this.$emit('address', result);
    },
  },
  created() {
    this.address =
      this.postDetail.address.sido +
      ' ' +
      this.postDetail.address.sigungu +
      ' ' +
      this.postDetail.address.bname;
  },
};
</script>
