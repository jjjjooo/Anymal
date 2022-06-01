<template>
  <v-app app>
    <core-toolbar> </core-toolbar>
    <v-fade-transition mode="out-in">
      <main v-bind="attrs">
        <core-jumbotron />

        <router-view />
      </main>
    </v-fade-transition>
  </v-app>
</template>

<script>
export default {
  components: {
    CoreToolbar: () => import('@/components/core/AppBar'),
    CoreJumbotron: () =>
      import('@/components/core/Jumbotron'),
  },
  computed: {
    attrs() {
      return {
        key: this.$route.path,
        style: {
          minHeight:
            'calc(100vh - ' +
            this.$vuetify.application.top +
            'px',
        },
      };
    },
  },
};
</script>

<style></style>
