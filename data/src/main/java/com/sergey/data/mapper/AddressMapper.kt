package com.sergey.data.mapper

import com.google.android.gms.maps.model.LatLng
import com.sergey.data.entity.Address
import com.sergey.data.entity.SendAddress
import com.sergey.domain.entity.LatLngDomain
import com.sergey.domain.entity.AddressDomain
import com.sergey.domain.mapper.InputMapper
import com.sergey.domain.mapper.OutputMapper

object AddressMapper : OutputMapper<AddressDomain, SendAddress>, InputMapper<SendAddress, AddressDomain> {

    override fun transformFromDomain(item: AddressDomain): SendAddress =
            SendAddress(LatLng(
                    item.startPosition.latitude,
                    item.startPosition.longitude),
                    LatLng(
                            item.endPosition.latitude,
                            item.endPosition.longitude))

    override fun transformToDomain(item: SendAddress): AddressDomain =
            AddressDomain(LatLngDomain(
                    item.startPosition.latitude,
                    item.startPosition.longitude),
                    LatLngDomain(
                            item.endPosition.latitude,
                            item.endPosition.longitude))
}