function toSignIn() {
  this.$router.push('/authentication/sign-in');
}
function toSignUp() {
  this.$router.push('/authentication/sign-up');
}

export { toSignIn, toSignUp };
