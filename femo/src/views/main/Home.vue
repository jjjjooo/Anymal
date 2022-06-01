<template>
  <section>
    <Preview />
  </section>
</template>

<script>
export default {
  components: {
    Preview: () => import('@/views/posts/Preview'),
  },
};
</script>

<style></style>
