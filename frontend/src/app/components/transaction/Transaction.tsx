import { useState,useEffect } from "react"
import { ITransaction } from "@/src/types/ITransaction"
import axios from "axios"


const Transaction:React.FC=() => {
    const [transactions, setTransactions] = useState<ITransaction[]>([]);
    const [accessToken,setAccessToken] = useState<string | null >(null);

    const BACKEND_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

   useEffect(() =>{
      const token = localStorage.getItem("access_token");
      setAccessToken(token);
   },[])

    useEffect(() => {
        if(!accessToken){
            return;
        }
      
        const fetchTransactions = async () => {
            try {
                const response = await axios.get(`${BACKEND_URL}/api/banca/transacciones/obtener-transacciones`,{
                    headers:{
                          Authorization: `Bearer ${accessToken}`
                    }
                });
                const data = response.data;
                setTransactions(data);

            } catch (error) {
                console.log(error);
            }
        }
        fetchTransactions();
    }, [accessToken])
  return (
    <div className="flex flex-col justify-start items-start space-y-4 overflow-y-auto max-h-[560px]">
        {transactions.map((transaction,id)=>
          <div className="flex flex-row justify-center items-center space-x-20 " key={id}>
           <div className="flex justify-center items-center w-[74.63px] h-[54px] bg-nav-buttons rounded-[13px]"><img src="/flecha-abajo.png" alt="flecha abajo " className="w-[44.22px] h-[32px]" /></div>

           <div className="flex flex-col justify-start items-start">
           <p className="text-[16px] font-bold">{transaction.transactionType}</p>
           <p className="text-[16px]">{transaction.date}</p>
           </div>
          <p className="text-middle-title text-[16px]">{transaction.amount > 0 ? `+$${transaction.amount}`: `-$${transaction.amount}`}</p>
           <p></p>
          </div>
        )}
    
    </div>
  )
}

export default Transaction