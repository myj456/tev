import React, { useContext } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider, AuthContext } from './contexts/AuthContext';
import Header from './components/Header';
import PrivateRoute from './components/PrivateRoute';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import BoardListPage from './pages/BoardListPage';
import BoardDetailPage from './pages/BoardDetailPage';
import BoardWritePage from './pages/BoardWritePage';
import AdminPage from './pages/AdminPage';
import UserDetailPage from './pages/UserDetailPage';
import BlockedScreen from './components/BlockedScreen';
import useAuth from './hooks/useAuth';

const AppContent = () => {
  const { isBlocked, blockReason, blockExpiryAt } = useAuth();

  if (isBlocked && blockReason && blockExpiryAt) {
    return <BlockedScreen reason={blockReason} expiryAt={blockExpiryAt} />;
  }

  return (
    <>
      <Header />
      <main className="bg-gray-50 min-h-screen">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/boards" element={<BoardListPage />} />
          <Route path="/boards/:boardId" element={<BoardDetailPage />} />
          
          {/* Protected Routes */}
          <Route element={<PrivateRoute />}>
            <Route path="/boards/new" element={<BoardWritePage />} />
            <Route path="/boards/edit/:boardId" element={<BoardWritePage />} />
            <Route path="/admin" element={<AdminPage />} />
            <Route path="/user/:userId" element={<UserDetailPage />} />
          </Route>
        </Routes>
      </main>
    </>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Router>
  );
}

export default App;