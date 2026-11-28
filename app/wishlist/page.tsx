"use client";
import {useEffect,useState} from "react";
import {ArrowLeft,Heart,Trash2} from "lucide-react";
type Car={id:string;make:string;model:string;year:number;priceKes:number;images:{url:string}[]};
const money=(n:number)=>"KSh "+Number(n).toLocaleString("en-KE");
export default function WishlistPage(){
 const [cars,setCars]=useState<Car[]>([]),[loading,setLoading]=useState(true),[error,setError]=useState("");
 async function load(){const r=await fetch("/api/customer/wishlist",{credentials:"include",cache:"no-store"});if(r.status===401||r.status===403){window.location.href="/account";return;}if(!r.ok)throw new Error("Could not load your saved cars.");setCars(await r.json());}
 useEffect(()=>{load().catch(e=>setError(e.message)).finally(()=>setLoading(false));},[]);
 async function logout(){const c=await fetch("/api/csrf",{credentials:"include"});const token=(await c.json()).token||"";await fetch("/api/customer/auth/logout",{method:"POST",credentials:"include",headers:{"X-XSRF-TOKEN":token}});window.location.href="/account";}
 async function remove(id:string){const c=await fetch("/api/csrf",{credentials:"include"});const token=(await c.json()).token||"";const r=await fetch("/api/customer/wishlist/"+id,{method:"DELETE",credentials:"include",headers:{"X-XSRF-TOKEN":token}});if(r.ok)setCars(v=>v.filter(x=>x.id!==id));}
 return <main className="wishlist-page"><div className="market-shell"><a className="customer-back" href="/"><ArrowLeft size={15}/> Back to cars</a><span className="market-eyebrow">YOUR SHORTLIST</span><button className="wishlist-signout" onClick={logout}>Sign out</button><h1>Cars you <em>love.</em></h1><p>Your saved vehicles are kept here for your next visit.</p>{loading?<p>Loading saved cars…</p>:error?<p>{error}</p>:cars.length===0?<div className="market-empty"><Heart/><h3>No saved cars yet</h3><p>Browse the collection and tap the heart on a vehicle to save it.</p><a className="market-pink-btn" href="/#inventory">Explore cars</a></div>:<div className="market-car-grid">{cars.map(v=><article className="market-car-card" key={v.id}><a className="market-car-image" href={"/cars/"+v.id}><img src={v.images?.[0]?.url||"/car-placeholder.svg"} alt={v.make+" "+v.model}/></a><div className="market-car-content"><small>{v.year}</small><h3>{v.make} {v.model}</h3><div className="market-car-bottom"><strong>{money(v.priceKes)}</strong><button onClick={()=>remove(v.id)} aria-label="Remove from wishlist"><Trash2/></button></div></div></article>)}</div>}</div></main>;
}
