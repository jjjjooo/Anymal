<template>
  <v-app>
    <v-container class="fill-height">
      <v-row justify="center">
        <v-col cols="auto" align="center">
          <ValidationObserver
            ref="observer"
            v-slot="{ invalid }"
          >
            <v-card width="400">
              <v-card-text class="text-center px-12 py-14">
                <div class="text-h4 font-weight-black">
                  회원가입
                </div>
              </v-card-text>
              <ValidationProvider
                v-slot="{ errors }"
                name="사용자 이름"
                rules="required|korAlphaNum"
              >
                <v-text-field
                  v-model="name"
                  label="사용자이름"
                  clearable
                  :error-messages="errors"
                  prepend-icon="mdi-email-outline"
                ></v-text-field>
              </ValidationProvider>
              <ValidationProvider
                v-slot="{ errors }"
                name="사용자 이름"
                rules="required|email"
              >
                <v-text-field
                  v-model="email"
                  label="이메일"
                  clearable
                  :error-messages="errors"
                  prepend-icon="mdi-email-outline"
                ></v-text-field>
              </ValidationProvider>
              <ValidationProvider
                v-slot="{ errors }"
                name="사용자 이름"
                rules="required|password"
              >
                <v-text-field
                  v-model="password"
                  label="비밀번호"
                  clearable
                  :error-messages="errors"
                  prepend-icon="mdi-lock-outline"
                  @click:append="show = !show"
                  :append-icon="
                    show ? 'mdi-eye' : 'mdi-eye-off'
                  "
                ></v-text-field>
              </ValidationProvider>

              <PostCode
                :updateFlag="true"
                @address="addrSelected"
              />
              <v-btn
                v-on:click="submitForm"
                class="mt-4"
                block
                x-large
                color="primary"
                :disabled="invalid"
                >회원가입</v-btn
              >
            </v-card>
          </ValidationObserver>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
import { registerMember } from '@/api/auth.js';

import PostCode from '@/components/PostCode.vue';
export default {
  components: {
    PostCode,
  },
  data() {
    return {
      postOpen: false,
      name: '',
      email: '',
      password: '',
      logMessage: '',
      address: [],
      show: false,
    };
  },

  methods: {
    addrSelected(data) {
      this.address.push(
        data.sido,
        data.sigungu,
        data.bname,
      );
      console.log(this.address);
    },
    async submitForm() {
      try {
        const memberData = {
          name: this.name,
          email: this.email,
          password: this.password,
          address: this.address,
        };
        const { data } = await registerMember(memberData);
        console.log(data);
        this.$router.push('/auth/sign-in');
      } catch (e) {
        console.log(e.error);
      }
    },
    initForm() {
      this.name = '';
      this.password = '';
      this.email = '';
    },
  },
  mounted() {},
};
</script>

<style></style>
