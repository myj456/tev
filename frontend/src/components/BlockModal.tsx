import React, { useState } from "react";

interface BlockModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: (reason: string, expiryDate: string) => void;
  userId: number;
}

const BlockModal: React.FC<BlockModalProps> = ({
  isOpen,
  onClose,
  onConfirm,
  userId,
}) => {
  const [reason, setReason] = useState("");
  const [expiryDate, setExpiryDate] = useState("");

  const handleConfirm = () => {
    onConfirm(reason, expiryDate);
    onClose();
  };

  if (!isOpen) {
    return null;
  }

  return (
    <div className='fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center'>
      <div className='bg-white p-6 rounded-lg shadow-xl'>
        <h2 className='text-lg font-bold mb-4'>사용자 차단</h2>
        <p>사용자 ID: {userId}</p>
        <div className='my-4'>
          <label
            htmlFor='reason'
            className='block text-sm font-medium text-gray-700'
          >
            차단 사유
          </label>
          <input
            type='text'
            id='reason'
            value={reason}
            onChange={(e) => setReason(e.target.value)}
            className='mt-1 block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm'
          />
        </div>
        <div className='my-4'>
          <label
            htmlFor='expiryDate'
            className='block text-sm font-medium text-gray-700'
          >
            차단 만료일
          </label>
          <input
            type='datetime-local'
            id='expiryDate'
            value={expiryDate}
            onChange={(e) => setExpiryDate(e.target.value)}
            className='mt-1 block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm'
          />
        </div>
        <div className='flex justify-end'>
          <button
            onClick={onClose}
            className='mr-2 px-4 py-2 bg-gray-300 text-black rounded hover:bg-gray-400'
          >
            취소
          </button>
          <button
            onClick={handleConfirm}
            className='px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700'
          >
            차단
          </button>
        </div>
      </div>
    </div>
  );
};

export default BlockModal;
