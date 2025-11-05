import React, {
  createContext,
  useState,
  useEffect,
  ReactNode,
  useCallback,
} from "react";
import { User, UserLogin, UserBlockResponse } from "../types/dto";
import * as authApi from "../api/authApi";

interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (userData: UserLogin) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
  isBlocked: boolean;
  blockReason: string | null;
  blockExpiryAt: string | null;
}

export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(
    localStorage.getItem("accessToken")
  );
  const [isBlocked, setIsBlocked] = useState(false);
  const [blockReason, setBlockReason] = useState<string | null>(null);
  const [blockExpiryAt, setBlockExpiryAt] = useState<string | null>(null);

  const fetchAndSetUser = useCallback(async () => {
    if (token) {
      try {
        const response = await authApi.fetchUserData();
        const userData = response.data.data;

        setUser({
          userId: userData.userId,
          email: userData.email,
          nickname: userData.nickname,
          role: userData.role,
          createdAt: userData.createdAt,
        });
      } catch (error) {
        console.error("Failed to fetch user details from /user/data:", error);
        // If fetching user data fails, it might be due to an invalid token
        // or the user being blocked. Let's handle logout.
        logout();
      }
    } else {
      setUser(null);
    }
  }, [token]);

  useEffect(() => {
    fetchAndSetUser();
  }, [fetchAndSetUser]);

  const login = async (userData: UserLogin) => {
    try {
      const response = await authApi.loginUser(userData);
      
      // Check if the user is blocked
      if (response.data.data && response.data.data.message) {
        const blockData: UserBlockResponse = response.data.data;
        setIsBlocked(true);
        setBlockReason(blockData.reason);
        setBlockExpiryAt(blockData.expiryAt);
        // Even if blocked, we might receive tokens, but we shouldn't store them
        // or proceed with login.
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        setToken(null);
        setUser(null);
        return; 
      }

      const { accessToken, refreshToken } = response.data.data;
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);
      setToken(accessToken);
      setIsBlocked(false);
      setBlockReason(null);
      setBlockExpiryAt(null);
      // After setting token, fetchAndSetUser will be called via useEffect
    } catch (error: any) {
        if (error.response && error.response.data && error.response.data.data && error.response.data.data.message) {
            const blockData: UserBlockResponse = error.response.data.data;
            setIsBlocked(true);
            setBlockReason(blockData.reason);
            setBlockExpiryAt(blockData.expiryAt);
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            setToken(null);
            setUser(null);
        } else {
            console.error("Login failed:", error);
            throw error;
        }
    }
  };

  const logout = () => {
    authApi.logoutUser().finally(() => {
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      setToken(null);
      setUser(null);
      setIsBlocked(false);
      setBlockReason(null);
      setBlockExpiryAt(null);
    });
  };

  const isAuthenticated = !!token && !isBlocked;

  return (
    <AuthContext.Provider
      value={{ user, token, login, logout, isAuthenticated, isBlocked, blockReason, blockExpiryAt }}
    >
      {children}
    </AuthContext.Provider>
  );
};
