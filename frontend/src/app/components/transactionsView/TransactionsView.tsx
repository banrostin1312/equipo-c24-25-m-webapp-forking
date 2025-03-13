"use client"
//Assets
import { useEffect, useState } from "react"
import Transaction from "../transaction/Transaction"
import NewTransfer from "../newTransfer/NewTransfer"
import axios from "axios"
import Link from "next/link"
//Context
import { useWebApp } from "@/src/context/WebappContext"

const TransactionsView: React.FC = () => {
    const [balance, setBalance] = useState<string | null>("")
    const [view, setView] = useState<"transactions" | "newTransfer">("transactions")
    const {accessToken} = useWebApp();

    const BACKEND_URL = process.env.NEXT_PUBLIC_BACKEND_URL;

   
        const getActualBalance = async () => {
            try {
                const response = await axios.get(`${BACKEND_URL}/api/banca/cuenta-bancaria/saldo`, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                        "Content-Type": "application/json"
                    }
                });
                setBalance(response.data.balance);
                console.log("GET SALDO CON EXITO",response.data);
            } catch (error) {
              console.log("ERROR AL TRAER EL SALDO",error);
            }
        }
        
        useEffect(()=> {
           getActualBalance()
        },[])

    return (
        <div className="flex flex-col md:justify-between md:flex-row px-20 space-y-4 md:space-y-14 mb-20">

            <div className="flex flex-col justify-center md:justify-between items-center space-y-6">
                <p className="text-[22px]">Mi Tarjeta</p>
                <img src="/credit-card.png" alt="credit-card-mockup" className="w-[331px] h-[199px]" />

                <div className="flex flex-col justify-center items-center bg-white border-estadocuenta-border border-[1px] rounded-[20px] 
                w-[331px] h-[105px]
                ">
                    <h3 className="text-[16px] text-letraestadodecuenta">Estado de cuenta </h3>
                    <p className="text-[20px]">${balance}</p>
                </div>

                <div className="flex flex-col justify-center items-center space-y-2">

                    <button>
                        <div className="flex flex-row justify-between items-center w-[328px] h-[45px] bg-nav-buttons rounded-[30px] px-6 hover:bg-buttons-hover hover:text-white">
                            <p className="text-[16px]">Agregar Tarjeta</p>
                            <img src="mas-icon.png" alt="+ icon" className="w-[20px] h-[20px]" />
                        </div>
                    </button>

                    <button onClick={() => setView("newTransfer")}>
                        <div className="flex flex-row justify-between items-center w-[328px] h-[45px] bg-nav-buttons rounded-[30px] px-6 hover:bg-buttons-hover hover:text-white">
                            <p className="text-[16px]">Nueva Transferencia</p>
                            <img src="mas-icon.png" alt="+ icon" className="w-[20px] h-[20px]" />
                        </div>
                    </button>

                    <button>
                        <div className="flex flex-row justify-between items-center w-[328px] h-[45px] bg-nav-buttons rounded-[30px] px-6 hover:bg-buttons-hover hover:text-white">
                            <p className="text-[16px]">Realizar un pago</p>
                            <img src="mas-icon.png" alt="+ icon" className="w-[20px] h-[20px]" />
                        </div>
                    </button>

                    <Link href={"/perfil"}>
                        <div className="flex flex-row justify-between items-center w-[328px] h-[45px] bg-nav-buttons rounded-[30px] px-6 hover:bg-buttons-hover hover:text-white">
                            <p className="text-[16px]">Mi Perfil</p>
                            <img src="mas-icon.png" alt="+ icon" className="w-[20px] h-[20px]" />
                        </div>
                    </Link>

                    
                </div>

            </div>

            {view === "transactions" ?
                <div className="flex flex-col items-center md:mt-20 w-full">
                    <div className="flex flex-col md:flex-row md:justify-between w-full px-11 justify-center items-center ">
                        <p className="text-[22px] text-nowrap">Historial de transacciones</p>
                        <button className="flex justify-center items-center bg-nav-buttons w-[86px] h-[45px] rounded-[30px] hover:text-white hover:bg-buttons-hover"><p className="text-[11px]">Ver Todo</p></button>
                    </div>
                    <Transaction />
                </div> : <NewTransfer goBack={() => setView("transactions")} />


            }

        </div>
    )
}

export default TransactionsView