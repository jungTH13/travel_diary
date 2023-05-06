<template>
<div class="login-progress-page">

    <div class="spinner-box">
        <span class="visually-hidden">로그인 중...</span>
    </div>

</div>
</template>

<style lang="scss" scoped>
.login-progress-page{
    position:absolute; 
    height:100%; 
    width:100%; 
    left:0px;
    top:0px; 
    z-index:500000;
    display: flex;
    background-color: rgba(255, 255, 255, 0.76);
    .spinner-box{
        margin:auto;
        font-size: 3rem;
        font-weight: 600;
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
  