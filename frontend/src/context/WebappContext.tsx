"use client"

import { useContext,createContext, ReactNode, useEffect, useState, Dispatch, SetStateAction } from "react";

interface webAppProps{
setAccessToken: Dispatch<SetStateAction<string | null>>;
accessToken:string | null
}


const webAppContext = createContext<webAppProps>({
    accessToken: null,
    setAccessToken: () => {},
  });

export const WebAppProvider:React.FC<{children:ReactNode}> = ({children}) =>{
const [accessToken,setAccessToken] = useState<string | null>(null);




return (
   <webAppContext.Provider value={{accessToken,setAccessToken}}>
    {children}
   </webAppContext.Provider>
)
} 

export const useWebApp = () => {
    const context = useContext(webAppContext)
    if (!context) {
        throw new Error("useWebApp must be used within a webApp Provider");
        
    }
    return context;
} 