// https://nuxt.com/docs/api/configuration/nuxt-config
// export default defineNuxtConfig({

// })
export default {
  // modules: ["@nuxtjs/vuex"],
  buildModules: ["@nuxtjs/style-resources"],

  // Style resources module configuration
  styleResources: {
    // your SCSS files location
    scss: ["@/assets/css/main.scss"],
  },
};
