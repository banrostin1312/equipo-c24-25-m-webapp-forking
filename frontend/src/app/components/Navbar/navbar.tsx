"use client"

//Assets
import Image from "next/image"
import Link from "next/link"
import { useState } from "react"
//Libraries
import { Menu, X } from "lucide-react"

const Navbar: React.FC = () => {

    const [isOpen,setIsopen] = useState(false);

    return (
       <nav>
        <button className="absolute top-5 right-5 z-50 md:hidden" onClick={() => setIsopen(!isOpen)}>
            {isOpen ? <X size={30} className="text-black"/> : < Menu size={30} className="text-black"/>}
        </button>

        <div className={`flex justify-center items-center fixed top-0 left-0 h-screen w-screen bg-black text-white
        ${isOpen ? "translate-x-0" : "-translate-x-full"}
         md:relative md:translate-x-0 md:h-auto md:w-auto md:bg-transparent
        `}
        >
            <ul className="flex flex-col justify-center items-center space-y-2 w-screen">
                <li> <Link href="/"><Image height={178} width={102} src="/LOGO.png" alt="Logo"></Image></Link></li>
                <li className="flex justify-center items-center bg-nav-buttons p-3 m-3 rounded-lg w-full h-[20px]"><Link href="/">Empresas</Link></li>
                <li className="flex justify-center items-center bg-nav-buttons p-3 m-3 rounded-lg w-full h-[20px]"><Link href="/">Personas</Link></li>
                <li className="flex justify-center items-center bg-nav-buttons p-3 m-3 rounded-lg w-full h-[20px]"><Link href="/">Turnos web</Link></li>
                <li className="flex justify-center items-center bg-nav-buttons p-3 m-3 rounded-lg w-full h-[20px]"><Link href="/">Ayuda</Link></li>
                <li className="flex justify-center items-center bg-nav-buttons p-3 m-3 rounded-lg w-full h-[20px]"><Link href="/">Homebanking</Link></li>
                <li className="flex justify-center items-center bg-nav-buttons p-3 m-3 rounded-lg w-full h-[20px] "><Link href="/">Registrate</Link></li>

            </ul>
        </div>

        </nav>
    )
}

export default Navbar