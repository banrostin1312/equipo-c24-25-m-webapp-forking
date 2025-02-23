"use client"
//Libraries 
//Assets
import Image from "next/image";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";

const Swipercarousel: React.FC = () => {
    return (
        <div className="flex justify-center items-center mt-7"> 
            <Swiper spaceBetween={10} slidesPerView={1} loop={true} className="md:w-full md:h-[430px] rounded-2xl h-[130px] w-[99%]">
                <SwiperSlide><Image alt="flayer1" src="/flayer1.png" objectFit="cover" layout="fill"></Image></SwiperSlide>
                <SwiperSlide><Image  objectFit="cover" layout="fill" src="/flayer 2.png" alt=""></Image></SwiperSlide>
                <SwiperSlide><Image  objectFit="cover" layout="fill" src="/flayer 3.png" alt=""></Image></SwiperSlide>
            </Swiper>
        </div>
    )
}

export default Swipercarousel