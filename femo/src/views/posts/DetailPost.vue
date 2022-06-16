<template>
  <ValidationObserver ref="observer" v-slot="{ invalid }">
    <v-container>
      <div v-if="!updateFlag">
        <v-row class="mt-2">
          <h2>{{ postDetail.title }}</h2>
          <v-chip-group
            class="ml-4"
            mandatory
            show-arrows
            active-class="primary--text"
          >
            <v-chip
              small
              outlined
              v-for="tag in postDetail.tag"
              :key="tag.id"
              @click="searchTag(tag.tag, tag.id)"
            >
              {{ tag.tag }}
            </v-chip>
          </v-chip-group>
        </v-row>
      </div>
      <div v-else>
        <v-row>
          <v-col cols="6">
            <ValidationProvider
              v-slot="{ errors }"
              name="제목"
              rules="required|max:30"
            >
              <v-text-field
                :error-messages="errors"
                v-model="postDetail.title"
                label="제목"
                hint="제목은 30자 미만으로 적어주세요"
              ></v-text-field>
            </ValidationProvider>
          </v-col>
          <v-col cols="6">
            <v-combobox
              v-model="chips"
              chips
              clearable
              multiple
              solo
            >
              <template
                v-slot:selection="{
                  attrs,
                  item,
                  select,
                  selected,
                }"
              >
                <v-chip
                  v-bind="attrs"
                  :input-value="selected"
                  close
                  @click="select"
                  @click:close="remove(item)"
                >
                  <strong>{{ item }}</strong
                  >&nbsp;
                </v-chip>
              </template>
            </v-combobox>
          </v-col>
        </v-row>
      </div>
      <v-row class="mb-4">
        <v-col align="start">
          <h5>
            {{ postDetail.username }} |
            {{ postDetail.lastTime }}
          </h5>
        </v-col>
        <v-col align="end">
          <v-btn
            color="pink"
            icon
            small
            :disabled="checkAuthority()"
            @click="pushGood(postDetail.id)"
          >
            <v-icon :disabled="!postGood">mdi-heart</v-icon>
          </v-btn>
          <v-btn
            class="float-right"
            small
            rounded
            :disabled="checkAuthority()"
            @click="chat"
          >
            <v-icon right> mdi-chat </v-icon>
          </v-btn>
        </v-col>
      </v-row>
      <v-row>
        <v-col cols="4">
          <v-select
            v-model="postDetail.gender"
            :items="['남', '여', '기타']"
            label="성별"
            outlined
            hint="성별을 선택해주세요"
          ></v-select>
        </v-col>
        <v-col cols="8">
          <div v-if="updateFlag">
            <post-code
              :updateFlag="updateFlag"
              :rAddress="postAddress"
              @address="addrSelected"
            />
          </div>
          <div v-else>
            <v-text-field
              text
              label="주소"
              v-model="rAddress"
              prepend-icon="mdi-email-outline"
              :disabled="!updateFlag"
            />
          </div>
        </v-col>
      </v-row>
      <v-spacer />
      <v-row class="mr-6 ml-6">
        <v-carousel hide-delimiters>
          <v-carousel-item
            v-for="(item, i) in postDetail.fileId"
            :key="i"
            :src="`http://localhost:9000/image/${item.fileId}`"
            reverse-transition="fade-transition"
            transition="fade-transition"
          ></v-carousel-item>
        </v-carousel>
      </v-row>
      <v-row>
        <v-col cols="12">
          <v-textarea
            v-if="postDetail && !updateFlag"
            background-color="white"
            class="mr-3 ml-3"
            no-resize
            outlined
            label="특징"
            v-model="postDetail.feature"
            readonly="readonly"
          ></v-textarea>
          <v-textarea
            v-else-if="updateFlag"
            v-model="updateDetail.feature"
            background-color="white"
            class="mr-3 ml-3"
            label="수정할 내용을 입력해주세요."
            outlined
          >
          </v-textarea>
        </v-col>
        <v-col>
          <div
            v-if="postDetail.username === username"
            class="float-right mt-2 ml-3"
          >
            <v-btn
              v-if="!updateFlag"
              dark
              @click="clickUpdateBtn"
            >
              수정
            </v-btn>

            <v-btn
              v-if="updateFlag"
              dark
              @click="clickUpdateBtn"
            >
              취소
            </v-btn>
            <v-btn
              v-if="updateFlag"
              class="ml-3"
              dark
              :disabled="invalid"
              @click="temp"
            >
              수정
            </v-btn>
            <v-btn
              class="mr-3 ml-3"
              dark
              @click="deletePost"
            >
              삭제
            </v-btn>
          </div>

          <v-file-input
            v-if="updateFlag"
            class="mr-3"
            label="사진 추가"
            multiple
            outlined
            v-model="images"
          >
          </v-file-input>
        </v-col>
      </v-row>
    </v-container>
  </ValidationObserver>
</template>

<script>
//import { mount } from '@vue/test-utils';
export default {
  name: 'postDetail',
  components: {
    PostCode: () => import('@/components/PostCode.vue'),
  },
  data() {
    return {
      good: false,
      updateFlag: false,
      images: [],
      chips: [],
      rChips: [],
      rAddress: '',
      updateDetail: {
        title: '',
        feature: '',
        tag: '',
        gender: '',
        address: { sido: '', sigungu: '', bname: '' },
        dtype: '',
        postId: '',
      },
    };
  },
  methods: {
    searchTag(tag, id) {
      this.$router.push({
        name: 'search',
        params: { tag: tag, id: id },
      });
    },
    addrSelected(data) {
      console.log(data);
      this.updateDetail.address = data;
    },
    remove(item) {
      this.chips.splice(this.chips.indexOf(item), 1);
      this.chips = [...this.chips];
    },
    chat() {
      let data = {
        postId: this.postDetail.id,
        sender: this.$store.state.memberStore.username,
        receiver: this.postDetail.username,
      };
      this.$store.dispatch('REQUEST_ADD_CHATROOM', data);
    },
    pushGood(postId) {
      this.$store.dispatch('REQUEST_PUSH_GOOD', postId);
    },
    deletePost() {
      if (this.checkAuthority()) {
        this.$store.dispatch(
          'REQUEST_DELETE_POST',
          this.postDetail.id,
        );
      }
    },
    clickUpdateBtn() {
      if (this.checkAuthority()) {
        this.updateFlag = !this.updateFlag;
      }
      this.updateDetail = this.postDetail;
      for (
        var index = 0;
        index < this.postDetail.tag.length;
        index++
      ) {
        this.chips.push(this.postDetail.tag[index].tag);
      }
      if (!this.updateFlag) {
        this.chips = [];
      }
    },
    checkAuthority() {
      if (this.username !== this.postDetail.username) {
        console.log('틀림');
        return false;
      }
      console.log('맞음');
      return true;
    },

    temp() {
      const postData = new FormData();
      let dto = {
        postId: this.postDetail.id,
        title: this.updateDetail.title,
        feature: this.updateDetail.feature,
        type2: this.updateDetail.dtype,
        tags: this.chips,
        gender: this.updateDetail.gender,
        removeTag: this.rChips,
        address: this.updateDetail.address,
      };

      postData.append(
        'key',
        new Blob([JSON.stringify(dto)], {
          type: 'application/json',
        }),
      );

      for (
        var index = 0;
        index < this.images.length;
        index++
      ) {
        postData.append('image', this.images[index]);
      }
      this.$store.dispatch('REQUEST_UPDATE_POST', postData);
      // .then(() => {
      //   this.$router.go(0);
      // });
      console.log(dto);
      console.log(this.updateDetail.address);
    },
  },
  computed: {
    postGood() {
      return this.$store.state.postStore.postGood;
    },
    postDetail() {
      return this.$store.state.postStore.postDetail;
    },
    username() {
      return this.$store.state.memberStore.username;
    },
    postList() {
      return this.$store.state.postStore.postList;
    },
    postAddress() {
      return this.$store.state.postStore.address;
    },
  },
  created() {
    this.$store
      .dispatch('REQUEST_GET_POST', this.$route.params.id)
      .then(() => {
        this.rAddress = this.$store.state.postStore.address;
      });
  },
};
</script>

<style scoped></style>
