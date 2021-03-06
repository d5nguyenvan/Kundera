/*******************************************************************************
 * * Copyright 2012 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.kundera.tests.crossdatastore.useraddress.datatype.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS", schema = "KunderaTests@addCassandra")
public class HabitatBi1To1FKBigDecimal
{
    @Id
    @Column(name = "ADDRESS_ID")
    private BigDecimal addressId;

    @Column(name = "STREET")
    private String street;

    @OneToOne(mappedBy = "address")
    private PersonnelBi1To1FKInt person;

    public BigDecimal getAddressId()
    {
        return addressId;
    }

    public void setAddressId(BigDecimal addressId)
    {
        this.addressId = addressId;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public PersonnelBi1To1FKInt getPerson()
    {
        return person;
    }

    public void setPerson(PersonnelBi1To1FKInt person)
    {
        this.person = person;
    }

}
