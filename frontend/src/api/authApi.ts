import api from "./axios";
import { UserCreate, UserLogin, User } from "../types/dto";

// 회원가입
export const registerUser = (userData: UserCreate) => {
  return api.post("/user/join", userData);
};

// 로그인
export const loginUser = (userData: UserLogin) => {
  return api.post("/user/login", userData);
};

// 로그아웃
export const logoutUser = () => {
  return api.post("/user/logout");
};

// 토큰 재발급
export const refreshToken = (token: string) => {
  return api.post("/user/refresh-token", { refreshToken: token });
};

// 로그인된 유저의 데이터를 조회
export const fetchUserData = () => {
  return api.get<{ data: User }>("/user/data");
};
