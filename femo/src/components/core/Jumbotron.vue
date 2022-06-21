<template>
  <v-img
    :height="$vuetify.breakpoint.xsOnly ? '30vh' : '300px'"
    gradient="to top, rgba(255, 255, 255, .75), rgba(255, 255, 255, .75)"
    :src="`https://placeimg.com/1200/400/animals/sepia`"
  >
    <v-container
      fill-height
      fluid
      :px-12="$vuetify.breakpoint.mdAndUp"
    >
      <v-row align-center>
        <v-slide-x-transition appear>
          <v-col align="center">
            <div
              class="mb-4"
              :class="[
                $vuetify.breakpoint.xsOnly
                  ? 'headline'
                  : 'display-2 font-weight-light',
              ]"
              justify
            ></div>
          </v-col>
        </v-slide-x-transition>
      </v-row>
    </v-container>
  </v-img>

  <!-- <v-sheet
    v-if="namespace === 'Index'"
    :min-height="
      $vuetify.breakpoint.smAndDown ? '40vh' : '300px'
    "
    color="grey lighten-3"
    height="20vh"
  >
    <v-container fill-height>
      <v-row
        align-content="center"
        justify="center"
        class="fill-height"
      >
        <v-col cols="12">
          <h1
            class="display-2 text-center"
            v-text="title"
          />
        </v-col>

        <v-breadcrumbs
          :items="breadcrumbs"
          class="grey--text pb-0 px-3"
        />
      </v-row>
    </v-container>
  </v-sheet> -->
</template>

<script>
export default {
  computed: {
    namespace() {
      return this.$route.name;
    },
    title() {
      switch (this.namespace) {
        default:
          return '';
      }
    },
  },
};
</script>
