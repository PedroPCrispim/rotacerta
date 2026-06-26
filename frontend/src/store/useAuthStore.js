import { create } from 'zustand';

const getStoredUser = () => {
  const storedUser = localStorage.getItem('user');
  if (!storedUser) {
    return null;
  }

  try {
    return JSON.parse(storedUser);
  } catch (error) {
    localStorage.removeItem('user');
    return null;
  }
};

const useAuthStore = create((set) => ({
  user: getStoredUser(),
  token: localStorage.getItem('token'),
  setAuth: (user, token) => {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    set({ user, token });
  },
  updateUser: (partialUser) =>
    set((state) => {
      const nextUser = state.user ? { ...state.user, ...partialUser } : null;
      if (nextUser) {
        localStorage.setItem('user', JSON.stringify(nextUser));
      }
      return { user: nextUser };
    }),
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    set({ user: null, token: null });
  },
}));

export default useAuthStore;
