<template>

    <div id="login-page">
      <h1 class="logo">TRAVEL DIARY</h1>
        <div id="login-box">
          <div id="login-button" >
              <button @click="oauthSignIn">구글 계정으로 로그인 하기</button>
          </div>
          <div v-if="env !== 'production'" id="login-button" >
              <button @click="examLogin">테스트 계정으로 로그인 하기</button>
          </div>
        </div>
    </div>

  <RouterView />
</template>

<style lang="scss">
#login-page {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;

  h1 {
    font-size: 5rem;
    font-weight: bold;
    letter-spacing: 0.2vh;
    color: $green !important;
    text-shadow: -1px -1px 0 rgb(255, 255, 255), 1px -1px 0 rgb(112, 112, 112), -5px 5px 5px rgb(112, 112, 112), 5px 5px 5px rgb(112, 112, 112);
  }
  #login-box{
    position: relative;
    top:30%;
    #login-button {
      box-shadow: 0 0 10px 5px $gray;
      border-radius: 50px;
      padding:0.5rem;
      padding-left: 2rem;
      padding-right:2rem;
      margin-bottom: 4rem;
      button {
        font-size:2.5rem;
        cursor: pointer;
        
      }
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
const store = useUserStore();

const env = store.env

async function examLogin() {
  const response = await store.examUserLogin();
  console.log(response)
  if(response.code === 200)
    router.replace("/");
  else
    alert("로그인에 실패 했습니다.")
}

function oauthSignIn() {
  // Google's OAuth 2.0 endpoint for requesting an access token
  var oauth2Endpoint = 'https://accounts.google.com/o/oauth2/v2/auth';

  // Create <form> element to submit parameters to OAuth 2.0 endpoint.
  var form = document.createElement('form');
  form.setAttribute('method', 'GET'); // Send as a GET request.
  form.setAttribute('action', oauth2Endpoint);

  // Parameters to pass to OAuth 2.0 endpoint.
  var params = {'client_id': import.meta.env.VITE_GOOGLE_OAUTH_ID,
                'redirect_uri': import.meta.env.VITE_GOOGLE_OAUTH_REDIRECT,
                'response_type': 'code',
                'scope': 'https://www.googleapis.com/auth/userinfo.email',
                'include_granted_scopes': 'true',
                'state': 'pass-through value'};

  // Add form parameters as hidden input values.
  for (var p in params) {
    var input = document.createElement('input');
    input.setAttribute('type', 'hidden');
    input.setAttribute('name', p);
    input.setAttribute('value', params[p]);
    form.appendChild(input);
  }

  // Add form to page and submit it to open the OAuth 2.0 endpoint.
  document.body.appendChild(form);
  form.submit();
}

onBeforeMount(async() => {
  console.log("Before Mount!");
  console.log(store.userInfo === {})
  const response = await store.getUserInfo()
  if(response.code === 200){
    alert("이미 로그인한 유저입니다!")
    router.push({
      name:"home"
    })
  }
});

onMounted(async () => {
  console.log("Mounted!");
});
</script>
