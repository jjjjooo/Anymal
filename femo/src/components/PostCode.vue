<template>
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
    rAddress: {
      type: String,
    },
  },

  components: {
    VueDaumPostcode,
  },
  data() {
    return {
      address: this.rAddress,
      postOpen: false,
    };
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
};
</script>
