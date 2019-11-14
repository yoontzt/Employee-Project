package com.axonactive.employeecore.address;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;
import com.axonactive.employeecore.presistence.GenericService;

@Stateless
public class AddressService extends GenericService<AddressEntity> {

	private static final String ADDRESS_NOT_FOUND = "Address is not found! Please check id again.";

	public AddressDTO findAddressByID(int addressID) {
		AddressDTO addressDTO = AddressConverter.getInstance().toDTO(em.find(AddressEntity.class, addressID));
		Optional<AddressDTO> addressOptional = Optional.ofNullable(addressDTO);
		if (!addressOptional.isPresent()) {
			throw new EntityNotFoundException(ADDRESS_NOT_FOUND);
		}
		return addressOptional.get();
	}

	private AddressEntity findAddressEntityById(int addressID) {
		AddressEntity addressEntity = em.find(AddressEntity.class, addressID);
		if (addressEntity == null) {
			throw new EntityNotFoundException(ADDRESS_NOT_FOUND);
		}
		return addressEntity;
	}

	public List<AddressDTO> getAllAddress() {
		TypedQuery<AddressEntity> query = em.createNamedQuery(AddressEntity.FIND_ALL, AddressEntity.class);
		List<AddressEntity> addressEntities = query.getResultList();
		if (!addressEntities.isEmpty())
			return AddressConverter.getInstance().toListDTO(addressEntities);
		throw new EntityNotFoundException(ADDRESS_NOT_FOUND);
	}

	public void addAddress(AddressDTO addressDTO) {
		validationCountry(addressDTO);
		validateID(addressDTO);
		AddressEntity addressEntity = AddressConverter.getInstance().toEntity(addressDTO);
		this.save(addressEntity);

	}

	public void update(AddressDTO addressDTO) {
		validationCountry(addressDTO);
		validateID(addressDTO);
		AddressEntity currentAddressEntity = findAddressEntityById(addressDTO.getAddressId());
		currentAddressEntity.setAddress(addressDTO.getAddress());
		currentAddressEntity.setCity(addressDTO.getCity());
		currentAddressEntity.setCountry(addressDTO.getCountry());
		this.update(currentAddressEntity);
	}

	public void deleteAddress(int addressID) {
		this.remove(findAddressEntityById(addressID));
	}

	private void validateID(AddressDTO addressDTO) {
		List<AddressEntity> listAddressEntity = em.createNamedQuery(AddressEntity.FIND_ALL_ID, AddressEntity.class)
				.getResultList();
		for (AddressEntity addressEntity : listAddressEntity) {
			if (addressEntity.getAddressId() == addressDTO.getAddressId()) {
				throw new ValidationException("The address is exist!");
			}
		}
	}

	private List<String> getListCountry() {
		String[] locales = Locale.getISOCountries();
		List<String> countries = new ArrayList<>();
		for (String countryCode : locales) {
			Locale local = new Locale("", countryCode);
			countries.add(local.getDisplayCountry());

		}

		return countries;
	}

	private boolean validationCountry(AddressDTO addressDTO) {
		List<String> countries = getListCountry();
		for (String string : countries) {
			if (string.equalsIgnoreCase(addressDTO.getCountry())) {
				return true;
			}
		}
		throw new ValidationException("Not match any country!");
	}

}
