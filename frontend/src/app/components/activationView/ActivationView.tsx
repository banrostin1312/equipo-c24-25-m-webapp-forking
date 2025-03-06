"use client"
//Assets
import { useState } from "react"
import axios from "axios";
import Swal from "sweetalert2";
import { useWebApp } from "@/src/context/WebappContext";
import { useRouter } from "next/navigation";
//Types
import { IVerification } from "@/src/types/IVerification";
const BACKEND_URL = process.env.NEXT_PUBLIC_BACKEND_URL;


const ActivationView: React.FC = () => {
   const {accessToken} = useWebApp();
   const router = useRouter();

    const [dataForm, setDataForm] = useState<IVerification>({
        document: "",
        birthDate: "",
        securityPhrase: "",
        dateOfActivation:new Date().toISOString().split("T")[0]
    });


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value, name } = event.target;

        setDataForm((prevState) => ({
            ...prevState,
            [name]: value,
        }))

    }

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        try {

            const response = await axios.post(`${BACKEND_URL}/api/banca/cuenta-bancaria/activar`, dataForm,{
                headers:{
                    Authorization:`Bearer ${accessToken}`,
                    "Content-Type": "application/json",
                }
            })
            console.log("SU CUENTA SE ACTIVO CORRECTAMENTE", response.data);
          
            Swal.fire({
                title: "Su cuenta se activo con exito",
                icon: "success",
                draggable: true
            });
              
            router.push("/transactions")
            setDataForm({
                document:"",
                birthDate:"",
                securityPhrase:"",
                dateOfActivation:""
            })
        } catch (error) {
          console.log("error al verificar cuenta:",error);
        }

    }

    return (
        <div>

            <div className="flex flex-col justify-center items-center md:w-[1115px] md:h-[583px] w-full">

                <div className="flex flex-col justify-center items-center bg-middle-title md:w-[1115px] md:h-[131px] w-full mt-4">
                    <img src="/LOGOsinbanca.png" alt="logo sin banca" className="w-[309px] h-[156px]" />
                </div>
                <div className="flex flex-col justify-center items-center mt-16 space-y-4">
                    <h5 className="text-[32px]">Verificación de cuenta</h5>
                    <form action="" className="flex flex-col justify-center items-center space-y-6" onSubmit={handleSubmit}>
                        <input type="text" placeholder="Documento" className="w-[401px] h-[44px] border-[1px] border-transfer-inputs rounded-[8px] placeholder:text-black placeholder:text-center text-center" name="document" value={dataForm.document} onChange={handleChange} />

                        <input type="text" placeholder="Fecha de nacimiento" className="w-[401px] h-[44px] border-[1px] border-transfer-inputs rounded-[8px]  placeholder:text-black placeholder:text-center text-center" name="birthDate" value={dataForm.birthDate} onChange={handleChange} />

                        <input type="text" placeholder="Frase de seguridad" className="w-[401px] h-[44px] border-[1px] border-transfer-inputs rounded-[8px]  placeholder:text-black placeholder:text-center text-center" name="securityPhrase" value={dataForm.securityPhrase} onChange={handleChange} />
                          
                        <input type="text" placeholder="Fecha de activacion" className="w-[401px] h-[44px] border-[1px] border-transfer-inputs rounded-[8px]  placeholder:text-black placeholder:text-center text-center" name="securityPhrase" value={dataForm.dateOfActivation} onChange={handleChange} readOnly />

                        <button className="flex justify-center items-center w-[121px] h-[45px] bg-nav-buttons rounded-[30px] hover:bg-buttons-hover hover:text-white">Verificar</button>
                    </form>
                </div>
            </div>

        </div>
    )
}

export default ActivationView