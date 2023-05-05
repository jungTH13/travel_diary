<template>
<div>
    <div style="position:fixed; height:100%; width:100%; left:0px;;top:0px; z-index:500000;">
    <div class="spinner-border m-1" role="status" style="left:50%; top:50%;">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
</div>
</template>

<style lang="scss">
#login-page {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;

    h1 {
    margin-top: 80px;
    }
    #login-button {
    margin-top: 40px;
    button {
        cursor: pointer;
    }
    }
}
</style>

<script setup>
import { computed, onBeforeMount, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "../stores/user";

const router = useRouter();
const route = useRoute()
const userStore = useUserStore();

const query = computed(()=>route.query)

onBeforeMount(() => {
    console.log("Before Mount!");
});

onMounted(async () => {
    console.log("Mounted!");

    if(query.value.code){
        const result = await userStore.googleOAuthLogin(query.value.code)
        if(result.code === 200){
            return router.push({
                name: 'home'
            })
        }
    }
    alert("로그인에 실패했습니다.\n다시 시도해 주세요")
    router.push({
        name: 'login'
    })
});
</script>
  