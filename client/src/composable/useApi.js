import axios from "axios";

export const useApi = () => {
  const BASE_URL = import.meta.env.VITE_SERVER_URL;

  const api = axios.create({
    baseURL: BASE_URL,
  });

  const get = async (endpoint) => {
    console.log(`%cGET 요청 ${BASE_URL + endpoint}`, "color: red");

    return api.get(endpoint, {
      withCredentials: true,
    });
  };

  const post = async (endpoint, bodyData) => {
    console.log(`%cPOST 요청 : ${endpoint}`, "color: red");
    console.log(`%cPOST 요청 데이터 :`, bodyData, "color: blue");
    console.log(bodyData);

    return api.post(endpoint, bodyData, {
      withCredentials: true,
      headers: {
        "Content-Type": "application/json",
      },
    });
  };

  const put = async (endpoint, bodyData) => {
    console.log(`%cPUT 요청 : ${endpoint}`, "color: red");
    console.log(`%cPUT 요청 데이터 : ${bodyData}`, "color: blue");

    return api.put(endpoint, bodyData, {
      withCredentials: true,
      headers: {
        "Content-Type": "application/json",
      },
    });
  };

  const del = async (endpoint) => {
    console.log(`%DELETE 요청 ${BASE_URL + endpoint}`, "color: red");

    return api.get(endpoint, {
      withCredentials: true,
    });
  };

  return {
    api,
    get,
    post,
    put,
    delete: del,
  };
};
