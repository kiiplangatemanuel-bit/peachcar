"use client";
import {useState} from "react";
import {Heart} from "lucide-react";
export default function WishlistButton({vehicleId}:{vehicleId:string}){
 const [saved,setSaved]=useState(false),[busy,setBusy]=useState(false);
 async function toggle(){if(busy)return;setBusy(true);try{const c=await fetch("/api/csrf",{credentials:"include"});const token=(await c.json()).token||"";const r=await fetch("/api/customer/wishlist/"+vehicleId,{method:"POST",credentials:"include",headers:{"X-XSRF-TOKEN":token}});if(r.status===401||r.status===403){window.location.href="/account";return;}if(!r.ok)throw new Error("Unable to save this vehicle.");setSaved(true);}catch{window.location.href="/account";}finally{setBusy(false);}}
 return <button type="button" className={"market-wishlist-button"+(saved?" saved":"")} onClick={toggle} disabled={busy} aria-label="Save vehicle to wishlist" title="Save to wishlist"><Heart size={16} fill={saved?"currentColor":"none"}/></button>;
}
