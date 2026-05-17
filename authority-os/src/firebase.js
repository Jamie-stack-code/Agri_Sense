import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyBs_wrtzyEYZSsdJDf41haYr1qqNooU8RA",
  authDomain: "agri-sense-bfbdb.firebaseapp.com",
  projectId: "agri-sense-bfbdb",
  storageBucket: "agri-sense-bfbdb.firebasestorage.app",
  messagingSenderId: "245402854299",
  appId: "1:245402854299:web:36dace4c7d130d3c5df73e"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
