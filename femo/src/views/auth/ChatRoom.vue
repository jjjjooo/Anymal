<template>
  <v-app>
    <v-container>
      <v-row justify="center">
        <h3>{{ this.username }}님의 채팅 목록</h3>
      </v-row>
      <v-row>
        <v-col
          v-for="(item, index) in chatRooms"
          :key="index"
          ><v-card>
            <div
              class="d-flex flex-no-wrap justify-space-between"
            >
              <div>
                <v-card-text class="text-h5"
                  >제목 : {{ item.postTitle }}</v-card-text
                >
                <v-card-subtitle>
                  {{ item.lastMessage }}
                  <small>
                    {{ item.updateDate.substring(0, 4) }}년
                    {{ item.updateDate.substring(5, 7) }}월
                    {{ item.updateDate.substring(8, 10) }}일
                    {{
                      item.updateDate.substring(11, 13)
                    }}시
                    {{
                      item.updateDate.substring(14, 16)
                    }}분
                  </small>
                </v-card-subtitle>
              </div>
              <div
                @click="
                  getReceiver(
                    item.sender,
                    item.receiver,
                    item.roomId,
                    item.productId,
                  )
                "
              >
                <v-badge
                  v-if="item.notRead !== 0"
                  :content="item.notRead"
                  class="float-right"
                  color="red"
                  overlap
                >
                </v-badge>
                <v-avatar size="90" class="mr-4 mt-4">
                  <v-img
                    :src="`http://localhost:9000/thumbnail/${item.image}`"
                  >
                  </v-img>
                </v-avatar>
              </div>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      receiver: '',
    };
  },
  methods: {
    getReceiver(name1, name2, roomId, productId) {
      if (this.username === name1) {
        this.receiver = name2;
      } else {
        this.receiver = name1;
      }
      this.goChatRoom(roomId, productId);
    },
    goChatRoom(roomId, productId) {
      this.$router.push({
        path: '/chat',
        query: {
          roomId: roomId,
          productId: productId,
          sender: this.username,
          receiver: this.receiver,
        },
      });
    },
  },
  computed: {
    chatRooms() {
      return this.$store.state.chatStore.chatRooms;
    },
    username() {
      console.log(this.$store.state.memberStore.username);
      return this.$store.state.memberStore.username;
    },
  },
  created() {},
  mounted() {
    this.$store.dispatch(
      'REQUEST_GET_CHAT_ROOMS',
      this.username,
    );
  },
};
</script>

<style scoped></style>
