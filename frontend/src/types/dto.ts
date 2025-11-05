export interface UserCreate {
  email: string;
  nickname: string;
  password?: string; // Optional for updates
}

export interface UserLogin {
  email: string;
  password: string;
}

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}

export interface BoardRequest {
  title: string;
  content: string;
}

export interface BoardUpdate {
  title?: string;
  content?: string;
}

export interface Board {
  boardId: number;
  title: string;
  content: string;
  nickname:string;
  viewCount: number;
  likeCount: number;
  createdAt: string;
  modifiedAt: string;
  commentList: Comment[];
  liked: boolean; // 좋아요 상태
}

export interface BoardList {
  boardId: number;
  title: string;
  content: string;
  nickname: string;
  createdAt: string;
  modifiedAt: string;
  viewCount: number;
  likeCount: number;
  liked: boolean; // 좋아요 상태
}

export interface Comment {
  commentId: number;
  content: string;
  nickname: string;
  createdAt: string;
  modifiedAt: string;
  boardId: number;
}

export interface CommentRequest {
  content: string;
}

export interface User {
  userId: number;
  email: string;
  nickname: string;
  role: string;
  createdAt: string;
  expiryAt?: string | null;
}

export interface UserListResponse extends User {}

export interface UserDetail extends User {
  boardList: BoardList[];
  commentList: Comment[];
  block?: UserBlockResponse; 
}

export interface UserBlockCreate {
  userId: number;
  reason: string;
  expiryAt: string; 
}

export interface UserBlockResponse {
  message: string;
  reason: string;
  expiryAt: string;
}
