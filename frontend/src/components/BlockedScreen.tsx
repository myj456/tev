
import React from 'react';

interface BlockedScreenProps {
  reason: string;
  expiryAt: string;
}

const BlockedScreen: React.FC<BlockedScreenProps> = ({ reason, expiryAt }) => {
  return (
    <div className="fixed inset-0 bg-gray-800 text-white flex flex-col justify-center items-center">
      <h1 className="text-3xl font-bold mb-4">계정이 차단되었습니다.</h1>
      <p className="mb-2">차단 사유: {reason}</p>
      <p>차단 만료일: {new Date(expiryAt).toLocaleString()}</p>
    </div>
  );
};

export default BlockedScreen;
