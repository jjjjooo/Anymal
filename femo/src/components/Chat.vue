<template>
  <v-app app>
    <v-container>
      <div class="float-left">
        <v-img
          :src="`http://localhost:9000/thumbnail/${chatRoomDetail.image}`"
          contain
          height="150"
          style="display: inline-block"
          width="150"
        >
        </v-img>
      </div>

      <div class="ml-5">
        <p>상대방 : {{ chatRoomDetail.postAuth }}</p>
        <p>게시글 제목 : {{ chatRoomDetail.postTitle }}</p>
      </div>

      <div style="clear: both"></div>
      <hr />

      <div
        id="chatDiv"
        style="height: 550px; overflow: scroll"
      >
        <div
          v-for="(list, index) in chatRoomDetail.chatLogs"
          :key="index"
        >
          <div v-if="list.sender === username">
            <div
              id="myMsg"
              class="float-right pt-3 pl-3 pr-3 rounded-lg text-center mt-5"
              style="
                border-radius: 10px;
                background-color: orange;
                color: white;
                height: auto;
                overflow: hidden;
                word-break: break-all;
                width: auto;
                max-width: 40%;
              "
            >
              <p>{{ list.message }}</p>
            </div>
            <div style="clear: both"></div>
            <p class="float-right">
              <small>{{
                list.createDate.substring(0, 16)
              }}</small>
            </p>
          </div>

          <div v-else>
            <div>
              <span class="float-left">{{
                list.sender
              }}</span>
              <div style="clear: both"></div>
              <div
                class="float-left pt-3 pl-3 pr-3 rounded-lg text-center"
                style="
                  border-radius: 10px;
                  background-color: darkgray;
                  height: auto;
                  overflow: hidden;
                  word-break: break-all;
                  width: auto;
                  max-width: 40%;
                "
              >
                <p>{{ list.message }}</p>
              </div>
            </div>
            <div style="clear: both"></div>
            <p class="float-left">
              <small>{{
                list.createDate.substring(0, 16)
              }}</small>
            </p>
          </div>

          <div style="clear: both"></div>
        </div>
      </div>
      <div id="bottomDiv">
        <v-row justify="center">
          <div>
            <v-text-field
              id="inputText"
              v-model="msg"
              class="mt-7"
              outlined
              style="width: 300px; display: inline-block"
              @keyup.enter="checkConnect"
            >
            </v-text-field>

            <v-btn
              color="orange"
              dark
              height="57"
              @click="checkConnect"
              >전송
            </v-btn>
          </div>
        </v-row>
      </div>
    </v-container>
  </v-app>
</template>

<script>
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
export default {
  name: 'Chat',
  data() {
    return {
      postDetail: '',
      postInfo: {
        title: '',
        seller: '',
        image: '',
      },
      roomId: '',
      msg: '',
      chatRequestDto: {
        message: '',
        sender: '',
        receiver: '',
        roomId: '',
        postId: '',
      },
    };
  },
  methods: {
    getChatLogs() {
      let getLogData = {
        roomId: this.$route.query.roomId,
        username: this.username,
      };
      this.$store
        .dispatch('REQUEST_GET_CHAT_LOGS', getLogData)
        .then(() => {
          this.$store.dispatch(
            'REQUEST_GET_CHAT_COUNT',
            this.username,
          );
        });
      document.getElementById('chatDiv').scrollTop =
        document.getElementById('chatDiv').scrollHeight;
    },
    sendMessage() {
      this.stompClient.send(
        '/pub/chat/msg',
        JSON.stringify({
          roomId: this.chatRequestDto.roomId,
          postId: this.chatRoomDetail.postId,
          sender: this.username,
          receiver: this.chatRequestDto.receiver,
          message: this.msg,
        }),
        {},
      );
      this.msg = '';
      document.getElementById('chatDiv').scrollTop =
        document.getElementById('chatDiv').scrollHeight;
    },
    checkConnect() {
      console.log(this.chatRequestDto.sender);
      console.log(this.chatRequestDto.receiver);
      if (!this.connected) {
        this.connect();
        setTimeout(() => {
          this.sendMessage();
        }, 300);
      } else {
        console.log('노타임아웃');
        this.sendMessage();
      }
    },
    connect() {
      this.socket = new SockJS(
        `${process.env.VUE_APP_API_URL}ws`,
      );

      this.stompClient = Stomp.over(this.socket);

      this.stompClient.connect(
        {},
        frame => {
          this.connected = true;
          console.log('소켓 연결 성공1', frame);

          this.stompClient.subscribe(
            '/sub/msg' + this.chatRequestDto.roomId,
            response => {
              console.log(
                '구독 :/send/msg/' +
                  this.chatRequestDto.roomId,
                response,
              );
              console.log(response.body);
              this.chatRoomDetail.chatLogs.push(
                JSON.parse(response.body),
              );
              document.getElementById('chatDiv').scrollTop =
                document.getElementById(
                  'chatDiv',
                ).scrollHeight;
            },
          );
        },
        error => {
          console.log('연결실패');
          console.log(error);
          this.connected = false;
        },
      );
    },
  },
  computed: {
    chatRoomDetail() {
      return this.$store.state.chatStore.chatRoomDetail;
    },
    username() {
      return this.$store.state.memberStore.username;
    },
    chatLogs() {
      return this.$store.state.chatStore.chatLog;
    },
  },
  created() {
    this.chatRequestDto.roomId = this.$route.query.roomId;
    this.chatRequestDto.postId = this.$route.query.postId;
    this.chatRequestDto.sender = this.$route.query.sender;
    this.chatRequestDto.receiver =
      this.$route.query.receiver;
    this.connect();
  },
  mounted() {
    this.getChatLogs();
  },
};
</script>

<style scoped></style>

<style></style>
