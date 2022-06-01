<template>
  <v-app>
    <v-container class="fill-height">
      <v-row>
        <v-col align="center">
          <ValidationObserver
            ref="observer"
            v-slot="{ invalid }"
          >
            <v-card width="400">
              <v-card-text class="text-center px-10 py-10">
                <div class="text-h4 font-weight-black">
                  로그인
                </div>
              </v-card-text>
              <ValidationProvider
                v-slot="{ errors }"
                name="이메일"
                rules="required|email"
              >
                <v-text-field
                  v-model="email"
                  label="이메일"
                  :error-messages="errors"
                  clearable
                  prepend-icon="mdi-email-outline"
                ></v-text-field>
              </ValidationProvider>
              <ValidationProvider
                v-slot="{ errors }"
                name="이메일"
                rules="required|password"
              >
                <v-text-field
                  v-model="password"
                  label="비밀번호를 입력해주세요"
                  clearable
                  :type="show ? 'text' : 'password'"
                  :error-messages="errors"
                  @click:append="show = !show"
                  prepend-icon="mdi-lock-outline"
                  :append-icon="
                    show ? 'mdi-eye' : 'mdi-eye-off'
                  "
                  class="mb-4"
                ></v-text-field>
              </ValidationProvider>
              <v-btn small text to="/auth/sign-up"
                >회원가입</v-btn
              >
              <v-btn
                v-on:click="login"
                class="mt-2"
                block
                x-large
                :disabled="invalid"
                color="primary"
                >로그인</v-btn
              >
            </v-card>
          </ValidationObserver>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      password: '',
      logMessage: '',
      show: false,
    };
  },

  methods: {
    async login() {
      try {
        const memberData = {
          email: this.email,
          password: this.password,
        };
        await this.$store.dispatch('LOGIN', memberData);
        this.$router.push('/');
      } catch (error) {
        alert(error.response.data.errorMessage);
        this.initForm();
      }
    },
    initForm() {
      this.email = '';
      this.password = '';
    },
  },
};
</script>

<style></style>
