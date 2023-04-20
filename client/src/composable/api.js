import axios from "axios";

const BASE_URL = import.meta.env.VITE_SERVER_URL;

const api = axios.create({
  baseURL: BASE_URL,
});

async function get(endpoint,body=null) {
  console.log(`%cGET 요청 ${BASE_URL + endpoint}`, "color: red");

  const config = {withCredentials: true}
  if(body) config.params = body;

  return api.get(endpoint, config);
}

async function post(endpoint, bodyData) {
  console.log(`%cPOST 요청 : ${endpoint}`, "color: red");
  console.log(`%cPOST 요청 데이터 :`, "color: blue");
  console.log(bodyData);

  return api.post(endpoint, bodyData, {
    withCredentials: true,
    headers: {
      "Content-Type": "application/json",
    },
  });
}

async function put(endpoint, bodyData) {
  console.log(`%cPUT 요청 : ${endpoint}`, "color: red");
  console.log(`%cPUT 요청 데이터 : ${bodyData}`, "color: blue");

  return api.put(endpoint, bodyData, {
    withCredentials: true,
    headers: {
      "Content-Type": "application/json",
    },
  });
}

async function del(endpoint) {
  console.log(`%DELETE 요청 ${BASE_URL + endpoint}`, "color: red");

  return api.get(endpoint, {
    withCredentials: true,
  });
}

// 아래처럼 export 한 후,
// import * as API 방식으로 가져오면
// API.get, API.delete 이런 식으로 쓸 수 있음
export { get, post, put, del as delete, api };
