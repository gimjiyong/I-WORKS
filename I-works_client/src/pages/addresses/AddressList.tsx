import axios from "axios"
import { useState, useEffect } from "react"
import { useParams } from "react-router"
import { getAccessToken } from "../../utils/auth"

interface UserData {
  userId: string,
  userEid: string,
  userNameFirst: string,
  userNameLast: string,
  departmentName: string,
  departmentId: string,
  positionCodeName: string,
  positionCodeId: string,
  userTel: string,
  userEmail: string
}


function AddressList() {
  const { departmentId = '' } = useParams<{ departmentId: string }>()
  const [userAll, setUserAll] = useState<UserData[]>([])

  useEffect(() => {
    axios.get(`https://suhyeon.site/api/address/user/all`, {
      headers: {
        Authorization: 'Bearer ' + getAccessToken(),
      }
    })
      .then((res) => {
        const data = res.data.data
        const filteredData = departmentId ? data.filter((user: UserData) => user.departmentId == departmentId) : data
        setUserAll(filteredData)
      })
      .catch((err) => {
        console.log(err)
      })
  }, [departmentId])

  return (
    <div className="">
      <div className="relative overflow-x-auto shadow-md sm:rounded-lg">
        <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th scope="col" className="px-6 py-3">
                이름
              </th>
              <th scope="col" className="px-6 py-3">
                직책/직급
              </th>
              <th scope="col" className="px-6 py-3">
                부서
              </th>
              <th scope="col" className="px-6 py-3">
                이메일
              </th>
              <th scope="col" className="px-6 py-3">
                전화번호
              </th>
            </tr>
          </thead>
          <tbody>
            {userAll.map((user) => (
              <tr key={user.userEid} className="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                <th scope="row" className="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap dark:text-white">
                  <div className="ps-3">
                    <div className="text-base font-semibold">{user.userNameLast}{user.userNameFirst}</div>
                  </div>
                </th>
                <td className="px-6 py-4">
                  {user.positionCodeName ? user.positionCodeName.substring(5) : ''}
                </td>
                <td className="px-6 py-4">
                  {user.departmentName}
                </td>
                <td className="px-6 py-4">
                  {user.userEmail}
                </td>
                <td className="px-6 py-4">
                  {user.userTel}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default AddressList
